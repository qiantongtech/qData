/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

/**
 * guideTipConfig usage instructions
 *
 * guideTipConfig is a configuration object that uniformly manages page prompt information and is used to display various prompt boxes on the front end.
 * By configuring different prompt IDs, you can flexibly control prompt content, style, and interactive behavior.
 *
 * Configuration object structure:
 *
export const guideTipConfig = {
    'Prompt ID': {
        title: 'Prompt box title, may contain HTML', // Prompt box title, displayed in the header
        content: 'Prompt content, which can include HTML or custom tags. If not set, the corresponding content will not be displayed', // Supports <span>, <a> tags and custom attributes (such as data-func, data-link)
        extensionLabel: 'Extension content label', // Optional label displayed before extension content, such as "Announcement"
        extensionContent: 'Extension content', // Optional extension area for announcements and other additional information; supports HTML
        type: 'prompt type', // Prompt type, used for style distinction, optional values: 'remind' (reminder), 'warning' (warning), 'danger' (danger), of which the 'danger' type cannot be set to "no more display"
        version: 'Configuration version number' // The currently configured version number, used for management and updates
    }
}
 *
 * Field description:
 *
 * title string prompt box title, displayed in the header
 * content string prompt content, which can contain plain text or HTML tags, such as <span>, <a>
 * extensionLabel string optional label displayed before extension content
 * extensionContent string optional extension content for announcements and other additional information; supports HTML tags
 * type string prompt type, used for style distinction, optional values: 'remind', 'warning', 'danger'
 * version string current configuration version number, used for management and updates
 *
 * Example:
export const guideTipConfig = {
    'cat/AttQualityCat.list': {
        title: 'Warm reminder! ',
        content: `
            This is under data assets
            <span class="clickable" data-func="routeTo" data-link="/da/dataQuality/dataQualityTasks">Data Quality Tasks</span>
            directory, visit
            <a href="http://example.com" target="_blank">Help Center</a> Learn more
        `,
        type: 'warning',
        version: '1.0'
    },
    'cat/AttAssetCat.detail': {
        title: 'Attention! ',
        content: 'Directory of data quality tasks under data assets',
        type: 'remind',
        version: '1.0'
    }
}
 *
 * Things to note:
 * 1. Unique prompt ID: The key of each prompt must be unique.
 * 2. HTML tag safety: Only use safe tags (such as <span>, <a>) and bind corresponding event logic.
 * 3. Version management: When updating content, version should be added to facilitate tracking and management.
 * 4. Style distinction: When the type is 'remind', it is a reminder style, and when it is 'warning', it is a warning style.
 * 5. Content length: If the content is too long, it can be divided into sections or new lines to ensure the display effect.
 */

// Define document base path
const DOCS_BASE_URL = 'https://qdata.qiantong.tech';

export const guideTipConfig = {
    // Home page
    'index': {
        title: '重要提醒：本系统为演示站点，<u>每日凌晨 02:30 清除所有用户数据</u>，以保障系统整洁与稳定运行。',
        content: `如需体验完整功能，请下载开源代码自行部署。如需 <u>开源版品牌授权</u> 或 <u>咨询商业版</u>，请点击查看详情： 👉  <a href="${DOCS_BASE_URL}/business.html" target="_blank">了解授权详情</a>`,
        extensionLabel: '公告',
        extensionContent: '<span class="guide-tip-announcement-brand">DataX来袭</span>，qData开源版 <span class="guide-tip-announcement-dict-tag">v1.6.0</span> 轻量发布！本次版本全新集成 DataX 数据同步能力，进一步<span class="guide-tip-announcement-keyword">精简部署架构与运行依赖</span>，<span class="guide-tip-announcement-keyword">降低资源占用和运维成本</span>，让数据接入、同步与任务管理更加简单、高效。',
        type: 'danger', version: '1.0'
    },

    /* Basic management */
    // audit rules
    'att/attAuditRule.list': {
        title: '稽查规则 —— 是保障数据质量的核心手段，基于 <u>标准数据元</u> 的定义（如字段类型、值域等）制定具体的检查逻辑，用于评估数据的质量表现。',
        content: `这些规则配置后，可被绑定到 <u>数据质量任务</u> 中，通过定时调度执行，自动扫描并识别数据异常，形成质量报告，支撑数据问题的发现与治理闭环。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/rule-management/audit-rules.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Cleaning rules
    'att/attCleanRule.list': {
        title: '清洗规则 —— 用于定义数据加工过程中的标准化处理逻辑，确保数据在集成过程中实现格式统一、值域规范、逻辑合理。',
        content: `您可基于 <u>标准数据元</u> 的定义创建清洗规则，并在 <u>数据集成任务</u> 中调用，对源端数据进行自动化清洗，保障进入中台的数据质量。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/rule-management/cleaning-rules.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // project management
    'att/attProject.list': {
        title: '项目 —— 是 <u>数据研发</u> 的工作空间',
        content: `用于按业务线或团队隔离管理数据连接、数据资产、任务和作业等。您只能访问所属项目的内容，请根据权限在对应项目中开展数据研发工作。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/project-management.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Topic management
    'att/attTheme.list': {
        title: '主题 —— 是 <u>数据资产</u> 分组维度之一，用于后续对数据资产进行主题化标注和分组管理。',
        content: `建议主题体系在平台初期由数据治理团队统一规划，保持主题颗粒度适中、语义明确，避免频繁调整带来管理混乱。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/topic-management.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Application management
    'att/client.list': {
        title: '应用 —— 用于对接第三方系统',
        content: `通过创建应用并生成专属密钥，实现对平台API的安全访问与权限控制。您可在此管理应用信息并获取调用凭证，确保服务间安全、可控地集成。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/application-management.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Data quality categories
    'cat/attQualityCat.list': {
        title: '数据质量类目是什么？',
        content: `数据质量类目用于对 <u>数据质量任务</u> 进行分类管理，帮助您快速定位和组织任务。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/category-management/data-quality-category.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Data resource category
    'cat/attApiCat.list': {
        title: '数据API服务目录是什么？',
        content: `数据API服务目录是 <u>数据服务</u> 模块中 API 服务的逻辑分类与目录化管理视图，实现接口资产的结构化组织、便捷检索和统一呈现。
        <a href="${DOCS_BASE_URL}/docs/user/basic-management/category-management/api-category.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },

    /* data standards */
    // logic model
    'dp/dpModel.list': {
        title: '逻辑模型 — 可视化设计与规范化的数据建模',
        content: `支持在业务层构建标准化的数据表结构，可从数据库导入或手动设计模型，并关联 <u>标准数据元</u> 实现字段级统一规范。支持一键物化为物理表，打通模型设计与数据落地的全流程。
        <a href="${DOCS_BASE_URL}/docs/user/data-standards/logical-model.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // standard data element
    'dp/dpDataElem.list': {
        title: '标准数据元 — 统一字段级数据定义与规范',
        content: `定义字段的名称、类型、长度、格式等标准属性，支持绑定代码表与清洗规则，为建模、<u>数据清洗</u> 和 <u>质量稽查</u> 提供统一依据，提升数据一致性与治理自动化水平。
        <a href="${DOCS_BASE_URL}/docs/user/data-standards/standard-data-element.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },

    /* data assets */
    // data connection
    'da/daDatasource.list': {
        title: '数据连接 — 统一管理多源异构数据接入',
        content: `用于配置和管理平台所需的数据源连接，支持关系型数据库、大数据平台、消息队列、文件服务及对象存储等多种类型。作为<u>数据采集</u>、<u>元数据同步</u>和<u>任务执行</u>的基础，保障<u>资产地图</u>等模块的数据可发现与可访问。
        <a href="${DOCS_BASE_URL}/docs/user/data-assets/data-connections.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Asset Map
    'da/daAsset.list': {
        title: '资产地图 — 全域数据资产的统一管理与全景洞察',
        content: `集中管理结构化数据表和非结构化文件等各类数据资产，提供资产概览、血缘关系、质量与清洗信息的全景视图。支持自动发现、手动接入与全生命周期治理，实现数据资产的可视、可管、可控，支撑数据高效发现与协同使用。
        <a href="${DOCS_BASE_URL}/docs/user/data-assets/asset-map.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Data query
    'da/executeSqlQuery': {
        title: '数据查询 — 自助式多源数据探索与分析',
        content: `提供可视化SQL查询界面，支持对关系型数据库和大数据平台的便捷访问。集成目录导航、语法补全与结果导出功能，帮助用户快速获取数据，满足分析验证、问题排查等场景需求，提升数据使用效率。
        <a href="${DOCS_BASE_URL}/docs/user/data-assets/data-query.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Data quality tasks
    'da/dataQuality/dataQualityTasks.list': {
        title: '数据质量任务 — 多维度数据质量监控与评估',
        content: `通过配置质量评测任务，对指定数据表执行完整性、准确性等多维度规则检查，生成质量报告并追踪问题数据。支持灵活调度与历史回溯，帮助用户及时发现数据异常，支撑数据治理闭环管理。
        <a href="${DOCS_BASE_URL}/docs/user/data-assets/data-quality/quality-tasks.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Sensitivity level
    'da/daSensitiveLevel/daSensitiveLevel.list': {
        title: '敏感等级 — 数据分级与安全管控的基础',
        content: `定义数据敏感级别，为字段和资产提供分类依据，并与脱敏规则自动关联。在数据预览、查询和输出等场景中，基于等级实施动态脱敏，保障敏感信息的安全与合规使用。
        <a href="${DOCS_BASE_URL}/docs/user/data-assets/data-security/sensitivity-level.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },

    /* Data research and development */
    // data connection project

    // data integration
    'dpp/tasker/dppEtlTask.list': {
        title: '数据集成任务 — 多源数据同步与转换的统一通道',
        content: `通过图形化方式配置ETL流程，支持关系型数据库、大数据平台、Kafka、HDFS等多源数据的接入与写出。提供丰富的转换组件，支持离线批处理与Flink实时流处理，实现数据清洗、转换与入湖入仓的一体化调度。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/task-management/integration-tasks.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // data development
    'dpp/tasker/dpptaskerddv.list': {
        title: '数据开发任务 — 实时与离线数据处理的统一编排',
        content: `支持基于Flink的实时流处理和Spark SQL的离线批处理，提供SQL开发、调试与调度执行能力。覆盖数据同步、计算加工与入湖入仓等场景，实现对多源数据的灵活处理与高效编排。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/task-management/development-tasks.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Task instance
    'dpp/etltaskinstance/dppEtlTaskInstance.list': {
        title: '运维管理 — 调度任务的执行监控与故障处理',
        content: `集中管理作业和数据开发任务的运行实例，支持查看执行日志、任务依赖关系与运行状态。提供实例重跑、日志下载等功能，助力运维人员高效监控调度链路，快速定位并处理执行异常。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/operations-management.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Task directory
    'dpp/dpp/attTaskCat': {
        title: '任务类目 — 数据研发任务的分类管理体系',
        content: `通过树形类目结构对数据集成等研发任务进行分类管理，支持多级目录配置与任务绑定。便于按业务或部门进行任务归类、快速筛选与统一维护，提升任务管理的条理性和协作效率。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/project-basic/integration-task-category.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Data development directory
    'dpp/dpp/AttDataDevCat.list': {
        title: '数据开发目录 — 数据开发任务的树状分类管理',
        content: `通过多层级目录结构对实时和离线数据开发任务进行分类组织，支持灵活创建与绑定类目。便于按业务场景或项目维度管理任务，实现快速查找与高效维护。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/project-basic/development-task-category.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    // Member role management
    'dpp/dpp/AttProjectUserRel.list': {
        title: '成员与角色管理 — 项目协作的权限基础',
        content: `通过成员管理和角色管理模块，实现项目内人员的添加、角色分配与细粒度权限控制。支持按职责定义角色并绑定权限，确保团队在安全隔离的环境下高效协作。
        <a href="${DOCS_BASE_URL}/docs/user/data-development/project-basic/member-role/" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },

    /* Data services */
    // API management
    'ds/dsApi.list': {
        title: 'API管理 — 数据服务的统一发布与共享',
        content: `支持通过向导式配置或SQL模式快速创建数据API，提供服务转发、权限控制与地理空间服务支持。便于安全、高效地开放和共享平台数据，满足内外部系统集成与应用调用需求。
        <a href="${DOCS_BASE_URL}/docs/user/data-services/api-management.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    /* data standards */
    // Standard search
    'dp/dpDocument': {
        title: '标准登记 — 统一纳管多源数据标准',
        content: `集中管理<u>国家标准</u>、<u>行业标准</u>、<u>地方标准</u>、<u>团体标准</u>等各类数据标准文件，支持上传、检索、版本控制与共享应用。通过统一归集与全生命周期管控，确保数据定义有据可依，支撑标准在建模、质量等环节的有效落地。
    `,
        type: 'remind', version: '1.0'
    },

    // Data modeling
    'dm/dataLayer.list': {
        title: '分层规范 — 自动化分层校验重塑数仓管理',
        content: `支持基于命名规范与业务维度自动校验数据层级（如维度、明细、汇总），提供从定义到调度的全生命周期管理。便于统一数据标准，降低模型维护成本，满足数据资产的高效治理与合规性要求。
    `,
        type: 'remind', version: '1.0'
    },
    // Data collection
    'mc/task/structured': {
        title: '采集任务 — 自动发现，统一纳管',
        content: `通过配置采集任务，从指定的数据源中获取库、表、字段等元数据，并按照平台规范进行解析、转换与入库。确保元数据的完整性、时效性与一致性，实现对全域数据资产的动态感知与集中纳管。
        <a href="https://community.qdata.tech/docs/user/data-governance/metadata-management/collect-tasks.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
    'meta/unreleased/structured/table': {
        title: '最新元数据 — 库级资产的核心单元维护载体',
        content: `管理表的结构信息、业务描述，可维护表的归属业务域、发布状态，也可更新采集而来的表级元数据。
        <a href="https://community.qdata.tech/docs/user/data-governance/metadata-management/latest-metadata.html" target="_blank">查看帮助文档</a>
    `,
        type: 'remind', version: '1.0'
    },
}
