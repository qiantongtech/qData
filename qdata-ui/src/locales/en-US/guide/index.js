// GuideTip translations
export default {
  index: {
    title: 'Important Notice: This system is a demo site. <u>All user data is cleared daily at 02:30 AM</u> to maintain system cleanliness and stable operation.',
    content: 'To experience full features, please download the open-source code for self-deployment. For <u>open-source brand authorization</u> or <u>commercial version consultation</u>, click to view details: 👉 <a href="https://qdata.qiantong.tech/business.html" target="_blank">Learn about authorization</a>',
    type: 'danger',
    version: '1.0'
  },
  attAuditRule: {
    title: 'Audit Rules — Core means for ensuring data quality, formulating specific check logic based on <u>standard data element</u> definitions (such as field types, value domains, etc.) to evaluate data quality performance.',
    content: 'After configuration, these rules can be bound to <u>data quality tasks</u> for scheduled execution, automatically scanning and identifying data anomalies, generating quality reports, and supporting data problem discovery and governance closure. <a href="https://qdata.qiantong.tech/docs/user/basic-management/rule-management/audit-rules.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attCleanRule: {
    title: 'Cleaning Rules — Used to define standardized processing logic in data processing, ensuring format consistency, domain specification, and logical reasonableness during data integration.',
    content: 'You can create cleaning rules based on <u>standard data element</u> definitions and invoke them in <u>data integration tasks</u> to automatically clean source data, ensuring data quality entering the platform. <a href="https://qdata.qiantong.tech/docs/user/basic-management/rule-management/cleaning-rules.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attProject: {
    title: 'Project — Workspace for <u>data development</u>',
    content: 'Used for isolated management of data connections, data assets, tasks, and jobs by business line or team. You can only access content within your project. Please carry out data development work in the corresponding project based on permissions. <a href="https://qdata.qiantong.tech/docs/user/basic-management/project-management.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attTheme: {
    title: 'Theme — One of the grouping dimensions for <u>data assets</u>, used for subsequent thematic annotation and group management of data assets.',
    content: 'It is recommended that the theme system be uniformly planned by the data governance team in the early stage of the platform, maintaining appropriate theme granularity and clear semantics, avoiding management confusion caused by frequent adjustments. <a href="https://qdata.qiantong.tech/docs/user/basic-management/topic-management.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attClient: {
    title: 'Application — Used for integrating with third-party systems',
    content: 'By creating applications and generating exclusive keys, achieve secure access and permission control for platform APIs. You can manage application information and obtain call credentials here, ensuring safe and controllable integration between services. <a href="https://qdata.qiantong.tech/docs/user/basic-management/application-management.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attQualityCat: {
    title: 'What is Data Quality Category?',
    content: 'Data Quality Category is used for classification management of <u>data quality tasks</u>, helping you quickly locate and organize tasks. <a href="https://qdata.qiantong.tech/docs/user/basic-management/category-management/data-quality-category.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attApiCat: {
    title: 'What is Data API Service Directory?',
    content: 'Data API Service Directory is the logical classification and directory management view for API services in the <u>data service</u> module, achieving structured organization, convenient retrieval, and unified presentation of interface assets. <a href="https://qdata.qiantong.tech/docs/user/basic-management/category-management/api-category.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dpModel: {
    title: 'Logical Model — Visual design and standardized data modeling',
    content: 'Supports building standardized data table structures at the business level, allowing import from databases or manual design, and associating with <u>standard data elements</u> for unified field-level specifications. Supports one-click materialization to physical tables, connecting the entire process from model design to data implementation. <a href="https://qdata.qiantong.tech/docs/user/data-standards/logical-model.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dpDataElem: {
    title: 'Standard Data Element — Unified field-level data definition and specification',
    content: 'Defines standard attributes such as field names, types, lengths, and formats. Supports binding code tables and cleaning rules, providing unified basis for modeling, <u>data cleaning</u>, and <u>quality audit</u>, improving data consistency and governance automation level. <a href="https://qdata.qiantong.tech/docs/user/data-standards/standard-data-element.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  daDatasource: {
    title: 'Data Connection — Unified management of multi-source heterogeneous data access',
    content: 'Used for configuring and managing data source connections required by the platform, supporting various types including relational databases, big data platforms, message queues, file services, and object storage. As the foundation for <u>data collection</u>, <u>metadata synchronization</u>, and <u>task execution</u>, ensuring data discoverability and accessibility for modules like <u>asset map</u>. <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-connections.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  daAsset: {
    title: 'Asset Map — Unified management and panoramic insight of domain-wide data assets',
    content: 'Centrally manages various data assets including structured data tables and unstructured files, providing panoramic views of asset overview, lineage relationships, quality and cleaning information. Supports automatic discovery, manual access, and full lifecycle governance, achieving visible, manageable, and controllable data assets, supporting efficient data discovery and collaborative use. <a href="https://qdata.qiantong.tech/docs/user/data-assets/asset-map.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  daAssetApply: {
    title: 'Asset Audit — Approval control for cross-project data usage',
    content: 'Manages usage applications for global data assets by projects, with approval by asset administrators. After approval, assets can be included in <u>project asset</u> lists for use in <u>data development tasks</u>, ensuring compliant data usage and controllable permissions, achieving balance between asset sharing and security. <a href="https://qdata.qiantong.tech/docs/user/data-assets/asset-audit.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  executeSqlQuery: {
    title: 'Data Query — Self-service multi-source data exploration and analysis',
    content: 'Provides visual SQL query interface, supporting convenient access to relational databases and big data platforms. Integrates directory navigation, syntax completion, and result export functions, helping users quickly obtain data, meeting needs for analysis verification, problem troubleshooting, and other scenarios, improving data usage efficiency. <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-query.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dataQualityTasks: {
    title: 'Data Quality Task — Multi-dimensional data quality monitoring and evaluation',
    content: 'By configuring quality evaluation tasks, perform multi-dimensional rule checks on specified data tables for completeness, accuracy, etc., generate quality reports and track problem data. Supports flexible scheduling and historical review, helping users timely discover data anomalies and support data governance closure management. <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-quality/quality-tasks.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  daSensitiveLevel: {
    title: 'Sensitivity Level — Foundation for data classification and security control',
    content: 'Defines data sensitivity levels, providing classification basis for fields and assets, and automatically associating with masking rules. In data preview, query, and output scenarios, implements dynamic masking based on levels, ensuring secure and compliant use of sensitive information. <a href="https://qdata.qiantong.tech/docs/user/data-assets/data-security/sensitivity-level.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dppAsset: {
    title: 'Project Asset — Isolation and use of data assets within projects',
    content: 'Centrally manages available data assets for current projects, supporting application from global assets or independent creation. Project members can perform field viewing, lineage analysis, quality and cleaning configuration within permission scope, and use for data development tasks, achieving secure isolation and compliant use. <a href="https://qdata.qiantong.tech/docs/user/data-development/project-assets.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dppEtlTask: {
    title: 'Data Integration Task — Unified channel for multi-source data synchronization and transformation',
    content: 'Configure ETL processes through graphical methods, supporting access and output of multi-source data including relational databases, big data platforms, Kafka, HDFS, etc. Provides rich transformation components, supporting offline batch processing and Flink real-time stream processing, achieving integrated scheduling of data cleaning, transformation, and lake/warehouse loading. <a href="https://qdata.qiantong.tech/docs/user/data-development/task-management/integration-tasks.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dpptaskerddv: {
    title: 'Data Development Task — Unified orchestration for real-time and offline data processing',
    content: 'Supports Flink-based real-time stream processing and Spark SQL-based offline batch processing, providing SQL development, debugging, and scheduled execution capabilities. Covers data synchronization, computational processing, and lake/warehouse loading scenarios, achieving flexible processing and efficient orchestration of multi-source data. <a href="https://qdata.qiantong.tech/docs/user/data-development/task-management/development-tasks.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dppEtlTaskInstance: {
    title: 'Operations Management — Execution monitoring and fault handling for scheduled tasks',
    content: 'Centrally manages running instances of jobs and data development tasks, supporting viewing execution logs, task dependencies, and running status. Provides instance re-run, log download, and other functions, helping operations personnel efficiently monitor scheduling links and quickly locate and handle execution exceptions. <a href="https://qdata.qiantong.tech/docs/user/data-development/operations-management.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  attTaskCat: {
    title: 'Task Category — Classification management system for data development tasks',
    content: 'Classifies and manages data integration and other development tasks through tree-shaped category structure, supporting multi-level directory configuration and task binding. Facilitates task classification, quick filtering, and unified maintenance by business or department, improving task management order and collaboration efficiency. <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/integration-task-category.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  AttDataDevCat: {
    title: 'Data Development Directory — Tree-shaped classification management for data development tasks',
    content: 'Classifies and organizes real-time and offline data development tasks through multi-level directory structure, supporting flexible creation and category binding. Facilitates task management by business scenario or project dimension, achieving quick lookup and efficient maintenance. <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/development-task-category.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  AttProjectUserRel: {
    title: 'Member and Role Management — Permission foundation for project collaboration',
    content: 'Through member management and role management modules, achieve personnel addition, role assignment, and fine-grained permission control within projects. Supports defining roles by responsibilities and binding permissions, ensuring team efficient collaboration in a securely isolated environment. <a href="https://qdata.qiantong.tech/docs/user/data-development/project-basic/member-role/" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dsApi: {
    title: 'API Management — Unified publishing and sharing of data services',
    content: 'Supports quick creation of data APIs through wizard configuration or SQL mode, providing service forwarding, permission control, and geospatial service support. Facilitates safe, efficient opening and sharing of platform data, meeting internal and external system integration and application invocation needs. <a href="https://qdata.qiantong.tech/docs/user/data-services/api-management.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  dpDocument: {
    title: 'Standard Registration — Unified management of multi-source data standards',
    content: 'Centrally manages various data standard documents including <u>national standards</u>, <u>industry standards</u>, <u>local standards</u>, and <u>group standards</u>, supporting upload, retrieval, version control, and shared application. Through unified collection and full lifecycle management, ensures data definitions have basis, supporting effective implementation of standards in modeling, quality, and other aspects.',
    type: 'remind',
    version: '1.0'
  },
  dataLayer: {
    title: 'Layering Specification — Automated layering verification reshaping data warehouse management',
    content: 'Supports automatic verification of data levels (such as dimensions, details, summaries) based on naming conventions and business dimensions, providing full lifecycle management from definition to scheduling. Facilitates unified data standards, reduces model maintenance costs, and meets efficient governance and compliance requirements for data assets.',
    type: 'remind',
    version: '1.0'
  },
  structured: {
    title: 'Collection Task — Automatic discovery, unified management',
    content: 'By configuring collection tasks, obtain metadata such as databases, tables, and fields from specified data sources, and parse, transform, and store according to platform specifications. Ensures metadata completeness, timeliness, and consistency, achieving dynamic perception and centralized management of domain-wide data assets. <a href="https://qdata.qiantong.tech/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  },
  unreleasedTable: {
    title: 'Latest Metadata — Core unit maintenance carrier for database-level assets',
    content: 'Manages table structural information and business descriptions, maintains table business domain and publishing status, and can update table-level metadata obtained from collection. <a href="https://qdata.qiantong.tech/user/%E6%95%B0%E6%8D%AE%E7%A0%94%E5%8F%91/%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86/2%E3%80%81%E6%95%B0%E6%8D%AE%E5%BC%80%E5%8F%91.html" target="_blank">View help documentation</a>',
    type: 'remind',
    version: '1.0'
  }
}
