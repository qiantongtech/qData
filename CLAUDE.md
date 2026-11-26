# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

qData is an open-source data platform (data integration, pipeline development, data modeling,
governance, catalog, data APIs, AI-assisted analytics). Backend: Java 8, Spring Boot 2.5.15,
Spring Security + JWT, MyBatis-Plus (+ MPJ Join), PageHelper, Druid — a RuoYi-style monolith.
Frontend: Vue 3 + Element Plus + Vite in plain JavaScript (not TypeScript). External engines:
DolphinScheduler (scheduling), Spark + DataX (ETL execution), RabbitMQ, Redis, Neo4j (lineage).
Supports MySQL, DM8, Kingbase8, Oracle as the application database.

## Commands

### Backend (Java 8, Maven 3.6+)
- Build all reactor modules: `mvn clean package -DskipTests` (root `pom.xml`)
- Run: start `tech.qiantong.qdata.server.QDataApplication` (module `qdata-server`), port 8080.
  Requires MySQL + Redis (see `qdata-server/src/main/resources/application-dev.yml`); RabbitMQ,
  Neo4j, DolphinScheduler for full functionality. Profile is `dev` by default
  (`spring.profiles.active` in `application.yml`).
- DB init: `sql/mysql/initialization/latest初始化.sql` (full dumps per version in the same dir;
  upgrades in `sql/{mysql,dm}/upgrade/<old> upgrade <new>/` — run `last-update.sql` then
  `last-update-data.sql`). Scripts exist for both MySQL and DM8 (达梦); maintain both for schema changes.
- Tests: all tests are `@SpringBootTest` integration tests needing a live environment —
  run one with `mvn test -pl qdata-server -Dtest=HttpUtilsTest`.
- `qdata-service-ai` is NOT part of the root reactor (own Spring AI build, runs standalone, port 8087).
  `qdata-service-quality` (port 8083) and `qdata-executor-etl` (Spark fat jar) build with the
  reactor but deploy separately.

### Frontend (`qdata-ui/`, npm — no lockfile committed)
- `npm install`
- `npm run dev` — dev server on port 81, proxies `/dev-api` → `http://localhost:8080` and
  `/dev-ai` → `http://localhost:8087`
- `npm run build:prod` / `build:stage` / `preview`
- `npm run eslint:lint` / `eslint:fix` (ESLint 9 flat config, `src` only)

### Docker
- `docker/qdata.sh` manages the compose stack (`docker/docker-compose*.yml`): base services,
  DolphinScheduler, qData itself, etc.

## Architecture

### Module layout

Every business module `qdata-module-X` is a Maven aggregator with two children:
- `qdata-module-X-api` — cross-module Java interfaces (`IXxxApiService`) + DTOs. Other modules
  depend only on this jar.
- `qdata-module-X-biz` — controllers/services/mappers, the real implementation. A biz
  `ServiceImpl` typically also implements the `-api` interface.

Module abbreviations (used in both Maven names and frontend `src/api/`, `src/views/` dirs):

| Module | Domain |
|---|---|
| `system` | users/roles/menus/dict, auth, monitoring |
| `att` | base management: catalogs, projects, tags, themes, source systems |
| `dp` | data standards: data elements, code maps, models |
| `dpp` | data development: ETL DAG editor, task/node instances, quality tasks, MQ listeners |
| `da` | data assets: datasources, assets, discovery |
| `ds` | data service: API publishing/portal (**not** DolphinScheduler) |
| `dm` | data modeling: theme domains, warehouse layers |
| `dg` | data governance: desensitization, sensitivity levels, whitelists |
| `mc` | metadata collection |
| `ai` | AI chat/model management (inside main server) |

Non-`module` pieces:
- `qdata-framework` — infra sub-modules: `qdata-common` (BaseEntity, CommonResult/AjaxResult,
  PageParam/PageResult, DbQuery dialect framework for 12+ DB types, DolphinScheduler HTTP client
  `DsRequestUtils`), `qdata-security` (Spring Security + JWT), `qdata-mybatis` (`BaseMapperX`,
  dynamic datasource), `qdata-redis`, `qdata-quartz` (`ISchedulerAdapter`), `qdata-generator`
  (code generator, routes under `/tool/gen`), `qdata-file`, `qdata-neo4j` (lineage), plus
  websocket/config/auth/pay.
- `qdata-server` — the deployable monolith; aggregates all `*-biz` jars and configuration.
- `qdata-api-ds` — DolphinScheduler REST client (`DsRequestUtils` + `QianTongDCApiType`).
- `qdata-executor-etl` — standalone Spark/DataX ETL jar, entry `tech.qiantong.qdata.spark.etl.EtlApplication`.
- `qdata-service-quality`, `qdata-service-ai` — standalone services.

### Business-module layering (follow this pattern for new CRUD)

Inside `qdata-module-X-biz/src/main/java/tech/qiantong/qdata/module/X/`:
`controller/admin/<domain>/XxxController.java` (extends `BaseController`,
`@PreAuthorize("@ss.hasPermi('dp:dataElem:list')")`, `@Log(...)` audit) with
`vo/` (`XxxPageReqVO` extends `PageParam`, `XxxRespVO`, `XxxSaveReqVO`) →
`convert/<domain>/XxxConvert.java` (MapStruct) → `dal/dataobject/<domain>/XxxDO.java`
(extends `BaseEntity`, `@TableName`, `@TableLogic` delFlag) + `dal/mapper/<domain>/XxxMapper.java`
(extends `BaseMapperX`, supports MPJ join queries) → `service/<domain>/IXxxService` +
`impl/XxxServiceImpl` (extends `ServiceImpl`, `@Transactional(rollbackFor = Exception.class)`).
XML mappers at `src/main/resources/mapper/<module>/<domain>/XxxMapper.xml` (UPPER_SNAKE columns →
camelCase resultMaps). The code generator (`qdata-generator`, `/tool/gen` endpoints, Velocity
templates in `qdata-generator/src/main/resources/vm/`) produces exactly this stack plus Vue page,
api.js, and menu SQL — prefer generating or copying an existing domain over inventing a new shape.

Responses: `CommonResult<T>` is primary; legacy `AjaxResult`/`TableDataInfo` still exist.
Pagination via `PageParam`/`PageResult` + PageHelper. Excel import/export via `ExcelUtil`.

### Auth & permissions

Stateless JWT (`Authorization` header, Redis-cached login user, `TokenService`). Method-level:
`@PreAuthorize("@ss.hasPermi('<module>:<domain>:<action>')")` — `ss` is
`PermissionService`. `@Anonymous` endpoints are whitelisted via `PermitAllUrlProperties`.
Dept data scoping via `@DataScope` + `DataScopeAspect` (no multi-tenancy).

### ETL / scheduling flow

`qdata-module-dpp` converts ETL DAG nodes to DolphinScheduler task definitions
(`utils/TaskConverter.java`, taskType SPARK or DATAX) and pushes them through `qdata-api-ds` →
DolphinScheduler REST API (`ds.base_url` in application-dev.yml). DS launches the
`qdata-executor-etl` Spark jar with a Base64 JSON pipeline config (reader → transitions →
writer). The executor stores incremental cursors in Redis and streams task status/logs over
RabbitMQ (`ds.exchange.taskInstance[.log]`, `ds.exchange.processInstance`), consumed by dpp
`listener/` classes to update instances shown in the UI. Internal timers use Quartz
(`qdata-quartz` `ISchedulerAdapter`). Quality tasks run in `qdata-service-quality` (:8083) and
metadata collection callbacks hit `qdata-module-mc` — both triggered by DS via `ds.*_url` config.

### Frontend (`qdata-ui/`)

- `src/api/<module>/` mirrors backend modules 1:1 (hand-written axios wrappers). System-module
  pages live in `src/views/sys/` though its api dir is `api/system`.
- Auto-import is ON: Vue/Pinia/vue-router APIs and Element Plus components need no imports.
  Global components (`Pagination`, `QtSearchBar`, `QtTable`, `QtWrap`, `DictTag`, ...) are
  registered in `main.js` — don't import them manually.
- Menus/routes come from the backend (`getRouters()`); a new view is invisible until a menu
  record (or manual route in `src/router/<module>/`) exists.
- `src/utils/request.js` adds `Authorization: Bearer` from the `Admin-Token` cookie, handles
  401/500/601 codes, dedups submits; PUT/DELETE may be rewritten to POST +
  `X-HTTP-Method-Override` per env flag.
- i18n: vue-i18n, locales in `src/locales/{zh-CN,en-US,ja-JP}/` with per-module namespaces;
  backend i18n in each module's `src/main/resources/i18n/`. UI text generally needs all three
  languages.
- SVG icons: `src/assets/icons/svg/`, use `<svg-icon icon-class="...">`.
- Login passwords are AES/RSA-encrypted client-side (`utils/aesEncrypt.js`, env keys).

## Conventions

- **License header**: every Java file carries the qData header (Apache 2.0 with additional
  qData terms). Copy it from any existing file into new ones — it must not be removed or altered.
- **Commits**: conventional commits with scope — `fix(etl): ...`, `feat(ui): ...`,
  `chore(sql): ...`, `docs(readme): ...`. Default branch is `develop`; PRs target it.
- **Versioning**: version lives in the root pom's `qdata.version` property (flatten-maven-plugin).
  Releases add a new `sql/{mysql,dm}/initialization/V<x.y.z>初始化.sql` plus
  `upgrade/V<old> upgrade V<new>/` scripts.
- Database SQL must be kept compatible across MySQL and DM8 (different dialects/quoting).
