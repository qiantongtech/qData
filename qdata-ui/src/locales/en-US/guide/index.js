// GuideTip component translation
const QDATA_COMMUNITY_URL = 'https://community.qdata.tech';

export default {
  index: {
    title: 'Important Notice: This system is a demo site. <u>All user data is cleared daily at 02:30 AM</u> to ensure system integrity and stable operation.',
    content: `To experience the full features, please download the open-source code and deploy it yourself. For <u>open-source brand licensing</u> or <u>commercial edition consultation</u>, click to view details: 👉  <a href="${QDATA_COMMUNITY_URL}/en/business/policy.html" target="_blank">Learn about licensing</a>`,
    extensionLabel: 'News',
    extensionContent: '<span class="guide-tip-announcement-brand">DataX is here</span> with the lightweight release of qData Open Source <span class="guide-tip-announcement-dict-tag">v1.6.0</span>! This version introduces DataX-powered data synchronization, <span class="guide-tip-announcement-keyword">streamlines the deployment architecture and runtime dependencies</span>, and <span class="guide-tip-announcement-keyword">reduces resource usage and maintenance costs</span> for simpler, faster data access, synchronization, and task management.',
    type: 'danger',
    version: '1.0'
  },
  attAuditRule: {
    title: 'Audit Rules — A core means to ensure data quality, based on <u>standard data element</u> definitions (such as field types, value domains, etc.) to establish specific check logic for evaluating data quality performance.',
    content: `Once configured, these rules can be bound to <u>data quality tasks</u>, executed through scheduled scheduling to automatically scan and identify data anomalies, generating quality reports to support the discovery and governance of data issues. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/rule-management/audit-rules.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attCleanRule: {
    title: 'Clean Rules — Used to define standardized processing logic during data processing, ensuring format uniformity, value domain standards, and logical correctness during data integration.',
    content: `You can create clean rules based on <u>standard data element</u> definitions and invoke them in <u>data integration tasks</u> to automatically clean source data, ensuring data quality entering the platform. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/rule-management/cleaning-rules.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attProject: {
    title: 'Projects — The workspace for <u>data development</u>',
    content: `Used to isolate and manage data connections, data assets, tasks, and jobs by business line or team. You can only access content within your assigned projects. Please conduct data development work according to your permissions. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/project-management.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attTheme: {
    title: 'Themes — One of the <u>data asset</u> grouping dimensions, used for thematic tagging and group management of data assets.',
    content: `It is recommended that the theme system be planned uniformly by the data governance team in the early stage of the platform, maintaining moderate granularity and clear semantics to avoid management confusion from frequent adjustments. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/topic-management.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attClient: {
    title: 'Applications — For integrating third-party systems',
    content: `Create applications and generate dedicated keys to enable secure API access and permission control. You can manage application information and obtain call credentials here, ensuring secure and controlled service integration. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/application-management.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attQualityCat: {
    title: 'What is a Data Quality Category?',
    content: `Data quality categories are used to classify and manage <u>data quality tasks</u>, helping you quickly locate and organize tasks. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/category-management/data-quality-category.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attApiCat: {
    title: 'What is the Data API Service Catalog?',
    content: `The Data API Service Catalog is the logical classification and directory management view of API services in the <u>data service</u> module, enabling structured organization, convenient retrieval, and unified presentation of interface assets. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/basic-management/category-management/api-category.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpModel: {
    title: 'Logical Model — Visual design and standardized data modeling',
    content: `Supports building standardized data table structures at the business layer, importing from databases or manually designing models, and associating <u>standard data elements</u> for field-level unified standards. Supports one-click materialization to physical tables, bridging model design and data implementation. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-standards/logical-model.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpDataElem: {
    title: 'Standard Data Element — Unified field-level data definitions and standards',
    content: `Defines standard attributes such as field name, type, length, and format. Supports binding code tables and clean rules, providing a unified basis for modeling, <u>data cleaning</u> and <u>quality auditing</u>, improving data consistency and governance automation. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-standards/standard-data-element.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  daDatasource: {
    title: 'Data Connection — Unified management of multi-source heterogeneous data access',
    content: `Configures and manages data source connections required by the platform, supporting relational databases, big data platforms, message queues, file services, and object storage. As the foundation for <u>data collection</u>, <u>metadata synchronization</u>, and <u>task execution</u>, it ensures data discoverability and accessibility for modules like <u>asset map</u>. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-assets/data-connections.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  daAsset: {
    title: 'Asset Map — Unified management and panoramic insight of domain-wide data assets',
    content: `Centrally manages various data assets including structured data tables and unstructured files, providing panoramic views of asset overview, lineage, quality, and cleaning information. Supports auto-discovery, manual access, and full lifecycle governance, making data assets visible, manageable, and controllable. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-assets/asset-map.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  executeSqlQuery: {
    title: 'Data Query — Self-service multi-source data exploration and analysis',
    content: `Provides a visual SQL query interface with convenient access to relational databases and big data platforms. Integrates directory navigation, syntax completion, and result export to help users quickly obtain data for analysis, verification, and troubleshooting scenarios. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-assets/data-query.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dataQualityTasks: {
    title: 'Data Quality Task — Multi-dimensional data quality monitoring and evaluation',
    content: `Configure quality evaluation tasks to perform multi-dimensional rule checks (completeness, accuracy, etc.) on specified data tables, generating quality reports and tracking problematic data. Supports flexible scheduling and historical review. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-assets/data-quality/quality-tasks.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  daSensitiveLevel: {
    title: 'Sensitivity Level — Foundation for data classification and security control',
    content: `Defines data sensitivity levels, providing classification basis for fields and assets, automatically linked with desensitization rules. Implements dynamic masking based on levels during data preview, query, and export scenarios. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-assets/data-security/sensitivity-level.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dppEtlTask: {
    title: 'Data Integration Task — Unified channel for multi-source data synchronization and transformation',
    content: `Configure ETL processes graphically, supporting relational databases, big data platforms, Kafka, HDFS, and other multi-source data input and output. Provides rich transformation components, supporting offline batch processing and Flink real-time stream processing. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/task-management/integration-tasks.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpptaskerddv: {
    title: 'Data Development Task — Unified orchestration for real-time and offline data processing',
    content: `Supports Flink-based real-time stream processing and Spark SQL offline batch processing, providing SQL development, debugging, and scheduled execution capabilities. Covers data synchronization, computational processing, and data lake/warehouse loading scenarios. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/task-management/development-tasks.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dppEtlTaskInstance: {
    title: 'Operations Management — Execution monitoring and fault handling for scheduled tasks',
    content: `Centrally manages running instances of jobs and data development tasks, supporting execution log viewing, task dependency relationships, and run status. Provides instance re-run and log download features. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/operations-management.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  attTaskCat: {
    title: 'Task Category — Classification management system for data development tasks',
    content: `Classifies and manages data integration and other development tasks through a tree category structure, supporting multi-level directory configuration and task binding. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/project-basic/integration-task-category.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  AttDataDevCat: {
    title: 'Data Development Catalog — Tree-structured classification management for data development tasks',
    content: `Organizes real-time and offline data development tasks through multi-level directory structures, supporting flexible category creation and binding. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/project-basic/development-task-category.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  AttProjectUserRel: {
    title: 'Member & Role Management — Permission foundation for project collaboration',
    content: `Through member management and role management modules, implement personnel addition, role assignment, and fine-grained permission control within projects. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-development/project-basic/member-role/" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dsApi: {
    title: 'API Management — Unified publishing and sharing of data services',
    content: `Supports rapid data API creation through wizard-style configuration or SQL mode, providing service forwarding, permission control, and geospatial service support. <a href="${QDATA_COMMUNITY_URL}/en/docs/user/data-services/api-management.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  dpDocument: {
    title: 'Standard Registry — Unified management of multi-source data standards',
    content: 'Centrally manages various data standard files including <u>national standards</u>, <u>industry standards</u>, <u>local standards</u>, and <u>group standards</u>, supporting upload, search, version control, and shared application.',
    type: 'remind',
    version: '1.0'
  },
  dataLayer: {
    title: 'Layer Specification — Automated layer verification reshaping data warehouse management',
    content: 'Supports automatic verification of data layers (dimension, detail, summary) based on naming conventions and business dimensions, providing full lifecycle management from definition to scheduling.',
    type: 'remind',
    version: '1.0'
  },
  structured: {
    title: 'Collection Task — Auto-discovery, unified management',
    content: `Configure collection tasks to obtain metadata (databases, tables, fields) from specified data sources, parsed, transformed, and stored according to platform standards. <a href="${QDATA_COMMUNITY_URL}/en/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  },
  unreleasedTable: {
    title: 'Latest Metadata — Core unit maintenance carrier for database-level assets',
    content: `Manages table structure information and business descriptions, maintaining table domain, publishing status, and collected table-level metadata. <a href="${QDATA_COMMUNITY_URL}/en/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">View documentation</a>`,
    type: 'remind',
    version: '1.0'
  }
}
