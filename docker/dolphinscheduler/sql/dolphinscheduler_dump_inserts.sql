--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: root
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: qrtz_blob_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);


ALTER TABLE public.qrtz_blob_triggers OWNER TO root;

--
-- Name: qrtz_calendars; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);


ALTER TABLE public.qrtz_calendars OWNER TO root;

--
-- Name: qrtz_cron_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);


ALTER TABLE public.qrtz_cron_triggers OWNER TO root;

--
-- Name: qrtz_fired_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(200) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);


ALTER TABLE public.qrtz_fired_triggers OWNER TO root;

--
-- Name: qrtz_job_details; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);


ALTER TABLE public.qrtz_job_details OWNER TO root;

--
-- Name: qrtz_locks; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);


ALTER TABLE public.qrtz_locks OWNER TO root;

--
-- Name: qrtz_paused_trigger_grps; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);


ALTER TABLE public.qrtz_paused_trigger_grps OWNER TO root;

--
-- Name: qrtz_scheduler_state; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


ALTER TABLE public.qrtz_scheduler_state OWNER TO root;

--
-- Name: qrtz_simple_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


ALTER TABLE public.qrtz_simple_triggers OWNER TO root;

--
-- Name: qrtz_simprop_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);


ALTER TABLE public.qrtz_simprop_triggers OWNER TO root;

--
-- Name: qrtz_triggers; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.qrtz_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);


ALTER TABLE public.qrtz_triggers OWNER TO root;

--
-- Name: t_ds_access_token_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_access_token_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_access_token_id_sequence OWNER TO root;

--
-- Name: t_ds_access_token; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_access_token (
    id integer DEFAULT nextval('public.t_ds_access_token_id_sequence'::regclass) NOT NULL,
    user_id integer,
    token character varying(64) DEFAULT NULL::character varying,
    expire_time timestamp without time zone,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_access_token OWNER TO root;

--
-- Name: t_ds_alert_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_alert_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_alert_id_sequence OWNER TO root;

--
-- Name: t_ds_alert; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_alert (
    id integer DEFAULT nextval('public.t_ds_alert_id_sequence'::regclass) NOT NULL,
    title character varying(512) DEFAULT NULL::character varying,
    sign character varying(40) DEFAULT ''::character varying NOT NULL,
    content text,
    alert_status integer DEFAULT 0,
    warning_type integer DEFAULT 2,
    log text,
    alertgroup_id integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    project_code bigint,
    process_definition_code bigint,
    process_instance_id integer,
    alert_type integer
);


ALTER TABLE public.t_ds_alert OWNER TO root;

--
-- Name: COLUMN t_ds_alert.sign; Type: COMMENT; Schema: public; Owner: root
--

COMMENT ON COLUMN public.t_ds_alert.sign IS 'sign=sha1(content)';


--
-- Name: t_ds_alert_plugin_instance; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_alert_plugin_instance (
    id integer NOT NULL,
    plugin_define_id integer NOT NULL,
    plugin_instance_params text,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    instance_name character varying(255),
    instance_type integer DEFAULT 0 NOT NULL,
    warning_type integer DEFAULT 3 NOT NULL
);


ALTER TABLE public.t_ds_alert_plugin_instance OWNER TO root;

--
-- Name: t_ds_alert_plugin_instance_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_alert_plugin_instance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_alert_plugin_instance_id_seq OWNER TO root;

--
-- Name: t_ds_alert_plugin_instance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_alert_plugin_instance_id_seq OWNED BY public.t_ds_alert_plugin_instance.id;


--
-- Name: t_ds_alert_send_status; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_alert_send_status (
    id integer NOT NULL,
    alert_id integer NOT NULL,
    alert_plugin_instance_id integer NOT NULL,
    send_status integer DEFAULT 0,
    log text,
    create_time timestamp without time zone
);


ALTER TABLE public.t_ds_alert_send_status OWNER TO root;

--
-- Name: t_ds_alert_send_status_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_alert_send_status_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_alert_send_status_id_seq OWNER TO root;

--
-- Name: t_ds_alert_send_status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_alert_send_status_id_seq OWNED BY public.t_ds_alert_send_status.id;


--
-- Name: t_ds_alertgroup_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_alertgroup_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_alertgroup_id_sequence OWNER TO root;

--
-- Name: t_ds_alertgroup; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_alertgroup (
    id integer DEFAULT nextval('public.t_ds_alertgroup_id_sequence'::regclass) NOT NULL,
    alert_instance_ids character varying(255) DEFAULT NULL::character varying,
    create_user_id integer,
    group_name character varying(255) DEFAULT NULL::character varying,
    description character varying(255) DEFAULT NULL::character varying,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_alertgroup OWNER TO root;

--
-- Name: t_ds_audit_log; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_audit_log (
    id integer NOT NULL,
    user_id integer NOT NULL,
    model_id bigint NOT NULL,
    model_name character varying(255) NOT NULL,
    model_type character varying(255) NOT NULL,
    operation_type character varying(255) NOT NULL,
    description character varying(255) NOT NULL,
    latency integer NOT NULL,
    detail character varying(255) DEFAULT NULL::character varying,
    create_time timestamp without time zone
);


ALTER TABLE public.t_ds_audit_log OWNER TO root;

--
-- Name: t_ds_audit_log_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_audit_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_audit_log_id_seq OWNER TO root;

--
-- Name: t_ds_audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_audit_log_id_seq OWNED BY public.t_ds_audit_log.id;


--
-- Name: t_ds_cluster; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_cluster (
    id integer NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    config text,
    description text,
    operator integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_cluster OWNER TO root;

--
-- Name: t_ds_cluster_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_cluster_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_cluster_id_seq OWNER TO root;

--
-- Name: t_ds_cluster_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_cluster_id_seq OWNED BY public.t_ds_cluster.id;


--
-- Name: t_ds_command_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_command_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_command_id_sequence OWNER TO root;

--
-- Name: t_ds_command; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_command (
    id integer DEFAULT nextval('public.t_ds_command_id_sequence'::regclass) NOT NULL,
    command_type integer,
    process_definition_code bigint NOT NULL,
    command_param text,
    task_depend_type integer,
    failure_strategy integer DEFAULT 0,
    warning_type integer DEFAULT 0,
    warning_group_id integer,
    schedule_time timestamp without time zone,
    start_time timestamp without time zone,
    executor_id integer,
    update_time timestamp without time zone,
    process_instance_priority integer DEFAULT 2,
    worker_group character varying(255),
    tenant_code character varying(64) DEFAULT 'default'::character varying,
    environment_code bigint DEFAULT '-1'::bigint,
    dry_run integer DEFAULT 0,
    process_instance_id integer DEFAULT 0,
    process_definition_version integer DEFAULT 0,
    test_flag integer
);


ALTER TABLE public.t_ds_command OWNER TO root;

--
-- Name: t_ds_datasource_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_datasource_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_datasource_id_sequence OWNER TO root;

--
-- Name: t_ds_datasource; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_datasource (
    id integer DEFAULT nextval('public.t_ds_datasource_id_sequence'::regclass) NOT NULL,
    name character varying(64) NOT NULL,
    note character varying(255) DEFAULT NULL::character varying,
    type integer NOT NULL,
    user_id integer NOT NULL,
    connection_params text NOT NULL,
    create_time timestamp without time zone NOT NULL,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_datasource OWNER TO root;

--
-- Name: t_ds_dq_comparison_type; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_comparison_type (
    id integer NOT NULL,
    type character varying NOT NULL,
    execute_sql character varying,
    output_table character varying,
    name character varying,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    is_inner_source boolean
);


ALTER TABLE public.t_ds_dq_comparison_type OWNER TO root;

--
-- Name: t_ds_dq_comparison_type_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_comparison_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_comparison_type_id_seq OWNER TO root;

--
-- Name: t_ds_dq_comparison_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_comparison_type_id_seq OWNED BY public.t_ds_dq_comparison_type.id;


--
-- Name: t_ds_dq_execute_result; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_execute_result (
    id integer NOT NULL,
    process_definition_id integer,
    process_instance_id integer,
    task_instance_id integer,
    rule_type integer,
    rule_name character varying(255) DEFAULT NULL::character varying,
    statistics_value double precision,
    comparison_value double precision,
    check_type integer,
    threshold double precision,
    operator integer,
    failure_strategy integer,
    state integer,
    user_id integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    comparison_type integer,
    error_output_path text
);


ALTER TABLE public.t_ds_dq_execute_result OWNER TO root;

--
-- Name: t_ds_dq_execute_result_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_execute_result_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_execute_result_id_seq OWNER TO root;

--
-- Name: t_ds_dq_execute_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_execute_result_id_seq OWNED BY public.t_ds_dq_execute_result.id;


--
-- Name: t_ds_dq_rule; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_rule (
    id integer NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    type integer,
    user_id integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_dq_rule OWNER TO root;

--
-- Name: t_ds_dq_rule_execute_sql; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_rule_execute_sql (
    id integer NOT NULL,
    index integer,
    sql text,
    table_alias character varying(255) DEFAULT NULL::character varying,
    type integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    is_error_output_sql boolean
);


ALTER TABLE public.t_ds_dq_rule_execute_sql OWNER TO root;

--
-- Name: t_ds_dq_rule_execute_sql_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_rule_execute_sql_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_rule_execute_sql_id_seq OWNER TO root;

--
-- Name: t_ds_dq_rule_execute_sql_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_rule_execute_sql_id_seq OWNED BY public.t_ds_dq_rule_execute_sql.id;


--
-- Name: t_ds_dq_rule_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_rule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_rule_id_seq OWNER TO root;

--
-- Name: t_ds_dq_rule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_rule_id_seq OWNED BY public.t_ds_dq_rule.id;


--
-- Name: t_ds_dq_rule_input_entry; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_rule_input_entry (
    id integer NOT NULL,
    field character varying(255) DEFAULT NULL::character varying,
    type character varying(255) DEFAULT NULL::character varying,
    title character varying(255) DEFAULT NULL::character varying,
    data character varying(255) DEFAULT NULL::character varying,
    options text,
    placeholder character varying(255) DEFAULT NULL::character varying,
    option_source_type integer,
    data_type integer,
    input_type integer,
    is_show smallint DEFAULT '1'::smallint,
    can_edit smallint DEFAULT '1'::smallint,
    is_emit smallint DEFAULT '0'::smallint,
    is_validate smallint DEFAULT '0'::smallint,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_dq_rule_input_entry OWNER TO root;

--
-- Name: t_ds_dq_rule_input_entry_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_rule_input_entry_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_rule_input_entry_id_seq OWNER TO root;

--
-- Name: t_ds_dq_rule_input_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_rule_input_entry_id_seq OWNED BY public.t_ds_dq_rule_input_entry.id;


--
-- Name: t_ds_dq_task_statistics_value; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_dq_task_statistics_value (
    id integer NOT NULL,
    process_definition_id integer NOT NULL,
    task_instance_id integer,
    rule_id integer NOT NULL,
    unique_code character varying NOT NULL,
    statistics_name character varying,
    statistics_value double precision,
    data_time timestamp(0) without time zone,
    create_time timestamp(0) without time zone,
    update_time timestamp(0) without time zone
);


ALTER TABLE public.t_ds_dq_task_statistics_value OWNER TO root;

--
-- Name: t_ds_dq_task_statistics_value_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_dq_task_statistics_value_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_dq_task_statistics_value_id_seq OWNER TO root;

--
-- Name: t_ds_dq_task_statistics_value_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_dq_task_statistics_value_id_seq OWNED BY public.t_ds_dq_task_statistics_value.id;


--
-- Name: t_ds_environment; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_environment (
    id integer NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    config text,
    description text,
    operator integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_environment OWNER TO root;

--
-- Name: t_ds_environment_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_environment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_environment_id_seq OWNER TO root;

--
-- Name: t_ds_environment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_environment_id_seq OWNED BY public.t_ds_environment.id;


--
-- Name: t_ds_environment_worker_group_relation; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_environment_worker_group_relation (
    id integer NOT NULL,
    environment_code bigint NOT NULL,
    worker_group character varying(255) NOT NULL,
    operator integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_environment_worker_group_relation OWNER TO root;

--
-- Name: t_ds_environment_worker_group_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_environment_worker_group_relation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_environment_worker_group_relation_id_seq OWNER TO root;

--
-- Name: t_ds_environment_worker_group_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_environment_worker_group_relation_id_seq OWNED BY public.t_ds_environment_worker_group_relation.id;


--
-- Name: t_ds_error_command; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_error_command (
    id integer NOT NULL,
    command_type integer,
    process_definition_code bigint NOT NULL,
    command_param text,
    task_depend_type integer,
    failure_strategy integer DEFAULT 0,
    warning_type integer DEFAULT 0,
    warning_group_id integer,
    schedule_time timestamp without time zone,
    start_time timestamp without time zone,
    executor_id integer,
    update_time timestamp without time zone,
    process_instance_priority integer DEFAULT 2,
    worker_group character varying(255),
    tenant_code character varying(64) DEFAULT 'default'::character varying,
    environment_code bigint DEFAULT '-1'::bigint,
    dry_run integer DEFAULT 0,
    message text,
    process_instance_id integer DEFAULT 0,
    process_definition_version integer DEFAULT 0,
    test_flag integer
);


ALTER TABLE public.t_ds_error_command OWNER TO root;

--
-- Name: t_ds_fav_task; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_fav_task (
    id integer NOT NULL,
    task_type character varying(64) NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE public.t_ds_fav_task OWNER TO root;

--
-- Name: t_ds_fav_task_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_fav_task_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_fav_task_id_seq OWNER TO root;

--
-- Name: t_ds_fav_task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_fav_task_id_seq OWNED BY public.t_ds_fav_task.id;


--
-- Name: t_ds_k8s; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_k8s (
    id integer NOT NULL,
    k8s_name character varying(255) DEFAULT NULL::character varying,
    k8s_config text,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_k8s OWNER TO root;

--
-- Name: t_ds_k8s_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_k8s_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_k8s_id_seq OWNER TO root;

--
-- Name: t_ds_k8s_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_k8s_id_seq OWNED BY public.t_ds_k8s.id;


--
-- Name: t_ds_k8s_namespace; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_k8s_namespace (
    id integer NOT NULL,
    code bigint NOT NULL,
    namespace character varying(255) DEFAULT NULL::character varying,
    user_id integer,
    cluster_code bigint NOT NULL,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_k8s_namespace OWNER TO root;

--
-- Name: t_ds_k8s_namespace_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_k8s_namespace_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_k8s_namespace_id_seq OWNER TO root;

--
-- Name: t_ds_k8s_namespace_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_k8s_namespace_id_seq OWNED BY public.t_ds_k8s_namespace.id;


--
-- Name: t_ds_listener_event; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_listener_event (
    id integer NOT NULL,
    content text,
    sign character varying(64) DEFAULT ''::character varying NOT NULL,
    post_status integer DEFAULT 0 NOT NULL,
    event_type integer NOT NULL,
    log text,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_listener_event OWNER TO root;

--
-- Name: COLUMN t_ds_listener_event.sign; Type: COMMENT; Schema: public; Owner: root
--

COMMENT ON COLUMN public.t_ds_listener_event.sign IS 'sign=sha1(content)';


--
-- Name: t_ds_plugin_define; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_plugin_define (
    id integer NOT NULL,
    plugin_name character varying(255) NOT NULL,
    plugin_type character varying(63) NOT NULL,
    plugin_params text,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_plugin_define OWNER TO root;

--
-- Name: t_ds_plugin_define_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_plugin_define_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_plugin_define_id_seq OWNER TO root;

--
-- Name: t_ds_plugin_define_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_plugin_define_id_seq OWNED BY public.t_ds_plugin_define.id;


--
-- Name: t_ds_process_definition_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_process_definition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_process_definition_id_sequence OWNER TO root;

--
-- Name: t_ds_process_definition; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_process_definition (
    id integer DEFAULT nextval('public.t_ds_process_definition_id_sequence'::regclass) NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    version integer DEFAULT 1 NOT NULL,
    description text,
    project_code bigint,
    release_state integer,
    user_id integer,
    global_params text,
    locations text,
    warning_group_id integer,
    flag integer,
    timeout integer DEFAULT 0,
    execution_type integer DEFAULT 0,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_process_definition OWNER TO root;

--
-- Name: t_ds_process_definition_log_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_process_definition_log_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_process_definition_log_id_sequence OWNER TO root;

--
-- Name: t_ds_process_definition_log; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_process_definition_log (
    id integer DEFAULT nextval('public.t_ds_process_definition_log_id_sequence'::regclass) NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    version integer DEFAULT 1 NOT NULL,
    description text,
    project_code bigint,
    release_state integer,
    user_id integer,
    global_params text,
    locations text,
    warning_group_id integer,
    flag integer,
    timeout integer DEFAULT 0,
    execution_type integer DEFAULT 0,
    operator integer,
    operate_time timestamp without time zone,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_process_definition_log OWNER TO root;

--
-- Name: t_ds_process_instance_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_process_instance_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_process_instance_id_sequence OWNER TO root;

--
-- Name: t_ds_process_instance; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_process_instance (
    id integer DEFAULT nextval('public.t_ds_process_instance_id_sequence'::regclass) NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    process_definition_code bigint,
    process_definition_version integer DEFAULT 1 NOT NULL,
    project_code bigint,
    state integer,
    state_history text,
    recovery integer,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    run_times integer,
    host character varying(135) DEFAULT NULL::character varying,
    command_type integer,
    command_param text,
    task_depend_type integer,
    max_try_times integer DEFAULT 0,
    failure_strategy integer DEFAULT 0,
    warning_type integer DEFAULT 0,
    warning_group_id integer,
    schedule_time timestamp without time zone,
    command_start_time timestamp without time zone,
    global_params text,
    process_instance_json text,
    flag integer DEFAULT 1,
    update_time timestamp without time zone,
    is_sub_process integer DEFAULT 0,
    executor_id integer NOT NULL,
    executor_name character varying(64) DEFAULT NULL::character varying,
    history_cmd text,
    dependence_schedule_times text,
    process_instance_priority integer DEFAULT 2,
    worker_group character varying(255),
    environment_code bigint DEFAULT '-1'::bigint,
    timeout integer DEFAULT 0,
    tenant_code character varying(64) DEFAULT 'default'::character varying,
    var_pool text,
    dry_run integer DEFAULT 0,
    next_process_instance_id integer DEFAULT 0,
    restart_time timestamp without time zone,
    test_flag integer
);


ALTER TABLE public.t_ds_process_instance OWNER TO root;

--
-- Name: t_ds_process_task_relation_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_process_task_relation_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_process_task_relation_id_sequence OWNER TO root;

--
-- Name: t_ds_process_task_relation; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_process_task_relation (
    id integer DEFAULT nextval('public.t_ds_process_task_relation_id_sequence'::regclass) NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    project_code bigint,
    process_definition_code bigint,
    process_definition_version integer,
    pre_task_code bigint,
    pre_task_version integer DEFAULT 0,
    post_task_code bigint,
    post_task_version integer DEFAULT 0,
    condition_type integer,
    condition_params text,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_process_task_relation OWNER TO root;

--
-- Name: t_ds_process_task_relation_log_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_process_task_relation_log_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_process_task_relation_log_id_sequence OWNER TO root;

--
-- Name: t_ds_process_task_relation_log; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_process_task_relation_log (
    id integer DEFAULT nextval('public.t_ds_process_task_relation_log_id_sequence'::regclass) NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    project_code bigint,
    process_definition_code bigint,
    process_definition_version integer,
    pre_task_code bigint,
    pre_task_version integer DEFAULT 0,
    post_task_code bigint,
    post_task_version integer DEFAULT 0,
    condition_type integer,
    condition_params text,
    operator integer,
    operate_time timestamp without time zone,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_process_task_relation_log OWNER TO root;

--
-- Name: t_ds_project_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_project_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_project_id_sequence OWNER TO root;

--
-- Name: t_ds_project; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_project (
    id integer DEFAULT nextval('public.t_ds_project_id_sequence'::regclass) NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    code bigint NOT NULL,
    description character varying(255) DEFAULT NULL::character varying,
    user_id integer,
    flag integer DEFAULT 1,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.t_ds_project OWNER TO root;

--
-- Name: t_ds_project_parameter_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_project_parameter_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_project_parameter_id_sequence OWNER TO root;

--
-- Name: t_ds_project_parameter; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_project_parameter (
    id integer DEFAULT nextval('public.t_ds_project_parameter_id_sequence'::regclass) NOT NULL,
    param_name character varying(255) NOT NULL,
    param_value text NOT NULL,
    code bigint NOT NULL,
    project_code bigint NOT NULL,
    user_id integer,
    operator integer,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.t_ds_project_parameter OWNER TO root;

--
-- Name: t_ds_project_preference_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_project_preference_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_project_preference_id_sequence OWNER TO root;

--
-- Name: t_ds_project_preference; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_project_preference (
    id integer DEFAULT nextval('public.t_ds_project_preference_id_sequence'::regclass) NOT NULL,
    code bigint NOT NULL,
    project_code bigint NOT NULL,
    preferences character varying(512) NOT NULL,
    user_id integer,
    state integer DEFAULT 1,
    create_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.t_ds_project_preference OWNER TO root;

--
-- Name: t_ds_queue_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_queue_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_queue_id_sequence OWNER TO root;

--
-- Name: t_ds_queue; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_queue (
    id integer DEFAULT nextval('public.t_ds_queue_id_sequence'::regclass) NOT NULL,
    queue_name character varying(64) DEFAULT NULL::character varying,
    queue character varying(64) DEFAULT NULL::character varying,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_queue OWNER TO root;

--
-- Name: t_ds_relation_datasource_user_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_datasource_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_datasource_user_id_sequence OWNER TO root;

--
-- Name: t_ds_relation_datasource_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_datasource_user (
    id integer DEFAULT nextval('public.t_ds_relation_datasource_user_id_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    datasource_id integer,
    perm integer DEFAULT 1,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_datasource_user OWNER TO root;

--
-- Name: t_ds_relation_namespace_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_namespace_user (
    id integer NOT NULL,
    user_id integer,
    namespace_id integer,
    perm integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_namespace_user OWNER TO root;

--
-- Name: t_ds_relation_namespace_user_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_namespace_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_namespace_user_id_seq OWNER TO root;

--
-- Name: t_ds_relation_namespace_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_relation_namespace_user_id_seq OWNED BY public.t_ds_relation_namespace_user.id;


--
-- Name: t_ds_relation_process_instance_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_process_instance_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_process_instance_id_sequence OWNER TO root;

--
-- Name: t_ds_relation_process_instance; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_process_instance (
    id integer DEFAULT nextval('public.t_ds_relation_process_instance_id_sequence'::regclass) NOT NULL,
    parent_process_instance_id integer,
    parent_task_instance_id integer,
    process_instance_id integer
);


ALTER TABLE public.t_ds_relation_process_instance OWNER TO root;

--
-- Name: t_ds_relation_project_user_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_project_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_project_user_id_sequence OWNER TO root;

--
-- Name: t_ds_relation_project_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_project_user (
    id integer DEFAULT nextval('public.t_ds_relation_project_user_id_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    project_id integer,
    perm integer DEFAULT 1,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_project_user OWNER TO root;

--
-- Name: t_ds_relation_project_worker_group_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_project_worker_group_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_project_worker_group_sequence OWNER TO root;

--
-- Name: t_ds_relation_project_worker_group; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_project_worker_group (
    id integer DEFAULT nextval('public.t_ds_relation_project_worker_group_sequence'::regclass) NOT NULL,
    project_code bigint,
    worker_group character varying(255) NOT NULL,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_project_worker_group OWNER TO root;

--
-- Name: t_ds_relation_resources_user_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_resources_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_resources_user_id_sequence OWNER TO root;

--
-- Name: t_ds_relation_resources_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_resources_user (
    id integer DEFAULT nextval('public.t_ds_relation_resources_user_id_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    resources_id integer,
    perm integer DEFAULT 1,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_resources_user OWNER TO root;

--
-- Name: t_ds_relation_rule_execute_sql; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_rule_execute_sql (
    id integer NOT NULL,
    rule_id integer,
    execute_sql_id integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_rule_execute_sql OWNER TO root;

--
-- Name: t_ds_relation_rule_execute_sql_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_rule_execute_sql_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_rule_execute_sql_id_seq OWNER TO root;

--
-- Name: t_ds_relation_rule_execute_sql_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_relation_rule_execute_sql_id_seq OWNED BY public.t_ds_relation_rule_execute_sql.id;


--
-- Name: t_ds_relation_rule_input_entry; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_rule_input_entry (
    id integer NOT NULL,
    rule_id integer,
    rule_input_entry_id integer,
    values_map text,
    index integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_rule_input_entry OWNER TO root;

--
-- Name: t_ds_relation_rule_input_entry_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_rule_input_entry_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_rule_input_entry_id_seq OWNER TO root;

--
-- Name: t_ds_relation_rule_input_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_relation_rule_input_entry_id_seq OWNED BY public.t_ds_relation_rule_input_entry.id;


--
-- Name: t_ds_relation_sub_workflow; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_sub_workflow (
    id integer NOT NULL,
    parent_workflow_instance_id bigint NOT NULL,
    parent_task_code bigint NOT NULL,
    sub_workflow_instance_id bigint NOT NULL
);


ALTER TABLE public.t_ds_relation_sub_workflow OWNER TO root;

--
-- Name: t_ds_relation_sub_workflow_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_sub_workflow_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_sub_workflow_id_seq OWNER TO root;

--
-- Name: t_ds_relation_sub_workflow_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_relation_sub_workflow_id_seq OWNED BY public.t_ds_relation_sub_workflow.id;


--
-- Name: t_ds_relation_udfs_user_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_relation_udfs_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_relation_udfs_user_id_sequence OWNER TO root;

--
-- Name: t_ds_relation_udfs_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_relation_udfs_user (
    id integer DEFAULT nextval('public.t_ds_relation_udfs_user_id_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    udf_id integer,
    perm integer DEFAULT 1,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_relation_udfs_user OWNER TO root;

--
-- Name: t_ds_resources_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_resources_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_resources_id_sequence OWNER TO root;

--
-- Name: t_ds_resources; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_resources (
    id integer DEFAULT nextval('public.t_ds_resources_id_sequence'::regclass) NOT NULL,
    alias character varying(64) DEFAULT NULL::character varying,
    file_name character varying(64) DEFAULT NULL::character varying,
    description character varying(255) DEFAULT NULL::character varying,
    user_id integer,
    type integer,
    size bigint,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    pid integer,
    full_name character varying(128),
    is_directory boolean DEFAULT false
);


ALTER TABLE public.t_ds_resources OWNER TO root;

--
-- Name: t_ds_schedules_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_schedules_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_schedules_id_sequence OWNER TO root;

--
-- Name: t_ds_schedules; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_schedules (
    id integer DEFAULT nextval('public.t_ds_schedules_id_sequence'::regclass) NOT NULL,
    process_definition_code bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone NOT NULL,
    timezone_id character varying(40) DEFAULT NULL::character varying,
    crontab character varying(255) NOT NULL,
    failure_strategy integer NOT NULL,
    user_id integer NOT NULL,
    release_state integer NOT NULL,
    warning_type integer NOT NULL,
    warning_group_id integer,
    process_instance_priority integer DEFAULT 2,
    worker_group character varying(255),
    tenant_code character varying(64) DEFAULT 'default'::character varying,
    environment_code bigint DEFAULT '-1'::bigint,
    create_time timestamp without time zone NOT NULL,
    update_time timestamp without time zone NOT NULL
);


ALTER TABLE public.t_ds_schedules OWNER TO root;

--
-- Name: t_ds_session; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_session (
    id character varying(64) NOT NULL,
    user_id integer,
    ip character varying(45) DEFAULT NULL::character varying,
    last_login_time timestamp without time zone
);


ALTER TABLE public.t_ds_session OWNER TO root;

--
-- Name: t_ds_task_definition_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_task_definition_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_task_definition_id_sequence OWNER TO root;

--
-- Name: t_ds_task_definition; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_task_definition (
    id integer DEFAULT nextval('public.t_ds_task_definition_id_sequence'::regclass) NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    version integer DEFAULT 1 NOT NULL,
    description text,
    project_code bigint,
    user_id integer,
    task_type character varying(50) DEFAULT NULL::character varying,
    task_execute_type integer DEFAULT 0,
    task_params text,
    flag integer,
    is_cache integer DEFAULT 0,
    task_priority integer DEFAULT 2,
    worker_group character varying(255) DEFAULT NULL::character varying,
    environment_code bigint DEFAULT '-1'::bigint,
    fail_retry_times integer,
    fail_retry_interval integer,
    timeout_flag integer,
    timeout_notify_strategy integer,
    timeout integer DEFAULT 0,
    delay_time integer DEFAULT 0,
    task_group_id integer,
    task_group_priority integer DEFAULT 0,
    resource_ids text,
    cpu_quota integer DEFAULT '-1'::integer NOT NULL,
    memory_max integer DEFAULT '-1'::integer NOT NULL,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_task_definition OWNER TO root;

--
-- Name: t_ds_task_definition_log_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_task_definition_log_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_task_definition_log_id_sequence OWNER TO root;

--
-- Name: t_ds_task_definition_log; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_task_definition_log (
    id integer DEFAULT nextval('public.t_ds_task_definition_log_id_sequence'::regclass) NOT NULL,
    code bigint NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    version integer DEFAULT 1 NOT NULL,
    description text,
    project_code bigint,
    user_id integer,
    task_type character varying(50) DEFAULT NULL::character varying,
    task_execute_type integer DEFAULT 0,
    task_params text,
    flag integer,
    is_cache integer DEFAULT 0,
    task_priority integer DEFAULT 2,
    worker_group character varying(255) DEFAULT NULL::character varying,
    environment_code bigint DEFAULT '-1'::bigint,
    fail_retry_times integer,
    fail_retry_interval integer,
    timeout_flag integer,
    timeout_notify_strategy integer,
    timeout integer DEFAULT 0,
    delay_time integer DEFAULT 0,
    resource_ids text,
    operator integer,
    task_group_id integer,
    task_group_priority integer DEFAULT 0,
    operate_time timestamp without time zone,
    cpu_quota integer DEFAULT '-1'::integer NOT NULL,
    memory_max integer DEFAULT '-1'::integer NOT NULL,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_task_definition_log OWNER TO root;

--
-- Name: t_ds_task_group; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_task_group (
    id integer NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    description character varying(255) DEFAULT NULL::character varying,
    group_size integer NOT NULL,
    project_code bigint DEFAULT '0'::bigint,
    use_size integer DEFAULT 0,
    user_id integer,
    status integer DEFAULT 1,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_task_group OWNER TO root;

--
-- Name: t_ds_task_group_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_task_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_task_group_id_seq OWNER TO root;

--
-- Name: t_ds_task_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_task_group_id_seq OWNED BY public.t_ds_task_group.id;


--
-- Name: t_ds_task_group_queue; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_task_group_queue (
    id integer NOT NULL,
    task_id integer,
    task_name character varying(255) DEFAULT NULL::character varying,
    group_id integer,
    process_id integer,
    priority integer DEFAULT 0,
    status integer DEFAULT '-1'::integer,
    force_start integer DEFAULT 0,
    in_queue integer DEFAULT 0,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_task_group_queue OWNER TO root;

--
-- Name: t_ds_task_group_queue_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_task_group_queue_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_task_group_queue_id_seq OWNER TO root;

--
-- Name: t_ds_task_group_queue_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_task_group_queue_id_seq OWNED BY public.t_ds_task_group_queue.id;


--
-- Name: t_ds_task_instance_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_task_instance_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_task_instance_id_sequence OWNER TO root;

--
-- Name: t_ds_task_instance; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_task_instance (
    id integer DEFAULT nextval('public.t_ds_task_instance_id_sequence'::regclass) NOT NULL,
    name character varying(255) DEFAULT NULL::character varying,
    task_type character varying(50) DEFAULT NULL::character varying,
    task_execute_type integer DEFAULT 0,
    task_code bigint NOT NULL,
    task_definition_version integer DEFAULT 1 NOT NULL,
    process_instance_id integer,
    process_instance_name character varying(255) DEFAULT NULL::character varying,
    project_code bigint,
    state integer,
    submit_time timestamp without time zone,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    host character varying(135) DEFAULT NULL::character varying,
    execute_path character varying(200) DEFAULT NULL::character varying,
    log_path text,
    alert_flag integer,
    retry_times integer DEFAULT 0,
    pid integer,
    app_link text,
    task_params text,
    flag integer DEFAULT 1,
    is_cache integer DEFAULT 0,
    cache_key character varying(200) DEFAULT NULL::character varying,
    retry_interval integer,
    max_retry_times integer,
    task_instance_priority integer,
    worker_group character varying(255),
    environment_code bigint DEFAULT '-1'::bigint,
    environment_config text,
    executor_id integer,
    executor_name character varying(64) DEFAULT NULL::character varying,
    first_submit_time timestamp without time zone,
    delay_time integer DEFAULT 0,
    task_group_id integer,
    var_pool text,
    dry_run integer DEFAULT 0,
    cpu_quota integer DEFAULT '-1'::integer NOT NULL,
    memory_max integer DEFAULT '-1'::integer NOT NULL,
    test_flag integer
);


ALTER TABLE public.t_ds_task_instance OWNER TO root;

--
-- Name: t_ds_tenant_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_tenant_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_tenant_id_sequence OWNER TO root;

--
-- Name: t_ds_tenant; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_tenant (
    id integer DEFAULT nextval('public.t_ds_tenant_id_sequence'::regclass) NOT NULL,
    tenant_code character varying(64) DEFAULT NULL::character varying,
    description character varying(255) DEFAULT NULL::character varying,
    queue_id integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_tenant OWNER TO root;

--
-- Name: t_ds_trigger_relation; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_trigger_relation (
    id integer NOT NULL,
    trigger_type integer NOT NULL,
    trigger_code bigint NOT NULL,
    job_id bigint NOT NULL,
    create_time timestamp without time zone,
    update_time timestamp without time zone
);


ALTER TABLE public.t_ds_trigger_relation OWNER TO root;

--
-- Name: t_ds_trigger_relation_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_trigger_relation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_trigger_relation_id_seq OWNER TO root;

--
-- Name: t_ds_trigger_relation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.t_ds_trigger_relation_id_seq OWNED BY public.t_ds_trigger_relation.id;


--
-- Name: t_ds_udfs_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_udfs_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_udfs_id_sequence OWNER TO root;

--
-- Name: t_ds_udfs; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_udfs (
    id integer DEFAULT nextval('public.t_ds_udfs_id_sequence'::regclass) NOT NULL,
    user_id integer NOT NULL,
    func_name character varying(255) NOT NULL,
    class_name character varying(255) NOT NULL,
    type integer NOT NULL,
    arg_types character varying(255) DEFAULT NULL::character varying,
    database character varying(255) DEFAULT NULL::character varying,
    description character varying(255) DEFAULT NULL::character varying,
    resource_id integer NOT NULL,
    resource_name character varying(255) NOT NULL,
    create_time timestamp without time zone NOT NULL,
    update_time timestamp without time zone NOT NULL
);


ALTER TABLE public.t_ds_udfs OWNER TO root;

--
-- Name: t_ds_user_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_user_id_sequence OWNER TO root;

--
-- Name: t_ds_user; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_user (
    id integer DEFAULT nextval('public.t_ds_user_id_sequence'::regclass) NOT NULL,
    user_name character varying(64) DEFAULT NULL::character varying,
    user_password character varying(64) DEFAULT NULL::character varying,
    user_type integer,
    email character varying(64) DEFAULT NULL::character varying,
    phone character varying(11) DEFAULT NULL::character varying,
    tenant_id integer DEFAULT '-1'::integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    queue character varying(64) DEFAULT NULL::character varying,
    state integer DEFAULT 1,
    time_zone character varying(32) DEFAULT NULL::character varying
);


ALTER TABLE public.t_ds_user OWNER TO root;

--
-- Name: COLUMN t_ds_user.state; Type: COMMENT; Schema: public; Owner: root
--

COMMENT ON COLUMN public.t_ds_user.state IS 'state 0:disable 1:enable';


--
-- Name: t_ds_version_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_version_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_version_id_sequence OWNER TO root;

--
-- Name: t_ds_version; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_version (
    id integer DEFAULT nextval('public.t_ds_version_id_sequence'::regclass) NOT NULL,
    version character varying(63) NOT NULL
);


ALTER TABLE public.t_ds_version OWNER TO root;

--
-- Name: t_ds_worker_group_id_sequence; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.t_ds_worker_group_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_ds_worker_group_id_sequence OWNER TO root;

--
-- Name: t_ds_worker_group; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.t_ds_worker_group (
    id bigint DEFAULT nextval('public.t_ds_worker_group_id_sequence'::regclass) NOT NULL,
    name character varying(255) NOT NULL,
    addr_list text,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    description text,
    other_params_json text
);


ALTER TABLE public.t_ds_worker_group OWNER TO root;

--
-- Name: t_ds_alert_plugin_instance id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert_plugin_instance ALTER COLUMN id SET DEFAULT nextval('public.t_ds_alert_plugin_instance_id_seq'::regclass);


--
-- Name: t_ds_alert_send_status id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert_send_status ALTER COLUMN id SET DEFAULT nextval('public.t_ds_alert_send_status_id_seq'::regclass);


--
-- Name: t_ds_audit_log id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_audit_log ALTER COLUMN id SET DEFAULT nextval('public.t_ds_audit_log_id_seq'::regclass);


--
-- Name: t_ds_cluster id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_cluster ALTER COLUMN id SET DEFAULT nextval('public.t_ds_cluster_id_seq'::regclass);


--
-- Name: t_ds_dq_comparison_type id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_comparison_type ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_comparison_type_id_seq'::regclass);


--
-- Name: t_ds_dq_execute_result id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_execute_result ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_execute_result_id_seq'::regclass);


--
-- Name: t_ds_dq_rule id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_rule_id_seq'::regclass);


--
-- Name: t_ds_dq_rule_execute_sql id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule_execute_sql ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_rule_execute_sql_id_seq'::regclass);


--
-- Name: t_ds_dq_rule_input_entry id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule_input_entry ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_rule_input_entry_id_seq'::regclass);


--
-- Name: t_ds_dq_task_statistics_value id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_task_statistics_value ALTER COLUMN id SET DEFAULT nextval('public.t_ds_dq_task_statistics_value_id_seq'::regclass);


--
-- Name: t_ds_environment id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment ALTER COLUMN id SET DEFAULT nextval('public.t_ds_environment_id_seq'::regclass);


--
-- Name: t_ds_environment_worker_group_relation id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment_worker_group_relation ALTER COLUMN id SET DEFAULT nextval('public.t_ds_environment_worker_group_relation_id_seq'::regclass);


--
-- Name: t_ds_fav_task id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_fav_task ALTER COLUMN id SET DEFAULT nextval('public.t_ds_fav_task_id_seq'::regclass);


--
-- Name: t_ds_k8s id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_k8s ALTER COLUMN id SET DEFAULT nextval('public.t_ds_k8s_id_seq'::regclass);


--
-- Name: t_ds_k8s_namespace id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_k8s_namespace ALTER COLUMN id SET DEFAULT nextval('public.t_ds_k8s_namespace_id_seq'::regclass);


--
-- Name: t_ds_plugin_define id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_plugin_define ALTER COLUMN id SET DEFAULT nextval('public.t_ds_plugin_define_id_seq'::regclass);


--
-- Name: t_ds_relation_namespace_user id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_namespace_user ALTER COLUMN id SET DEFAULT nextval('public.t_ds_relation_namespace_user_id_seq'::regclass);


--
-- Name: t_ds_relation_rule_execute_sql id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_rule_execute_sql ALTER COLUMN id SET DEFAULT nextval('public.t_ds_relation_rule_execute_sql_id_seq'::regclass);


--
-- Name: t_ds_relation_rule_input_entry id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_rule_input_entry ALTER COLUMN id SET DEFAULT nextval('public.t_ds_relation_rule_input_entry_id_seq'::regclass);


--
-- Name: t_ds_relation_sub_workflow id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_sub_workflow ALTER COLUMN id SET DEFAULT nextval('public.t_ds_relation_sub_workflow_id_seq'::regclass);


--
-- Name: t_ds_task_group id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_group ALTER COLUMN id SET DEFAULT nextval('public.t_ds_task_group_id_seq'::regclass);


--
-- Name: t_ds_task_group_queue id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_group_queue ALTER COLUMN id SET DEFAULT nextval('public.t_ds_task_group_queue_id_seq'::regclass);


--
-- Name: t_ds_trigger_relation id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_trigger_relation ALTER COLUMN id SET DEFAULT nextval('public.t_ds_trigger_relation_id_seq'::regclass);


--
-- Data for Name: qrtz_blob_triggers; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_calendars; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_cron_triggers; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_1', 'jobgroup_3', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_2', 'jobgroup_2', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_3', 'jobgroup_3', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_4', 'jobgroup_2', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_5', 'jobgroup_2', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_6', 'jobgroup_2', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_7', 'jobgroup_2', '0 0 0 1 * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_15', 'jobgroup_3', '0 0 0 * * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_18', 'jobgroup_3', '0 0 0 * * ?', 'Asia/Shanghai');
INSERT INTO public.qrtz_cron_triggers VALUES ('DolphinScheduler', 'job_17', 'jobgroup_3', '0 0 0 1 * ?', 'Asia/Shanghai');


--
-- Data for Name: qrtz_fired_triggers; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_job_details; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_1', 'jobgroup_3', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000374000a7363686564756c6549647371007e0008000000017800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_2', 'jobgroup_2', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000274000a7363686564756c65496471007e000a7800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_3', 'jobgroup_3', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000374000a7363686564756c65496471007e000a7800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_4', 'jobgroup_2', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000274000a7363686564756c6549647371007e0008000000047800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_5', 'jobgroup_2', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000274000a7363686564756c6549647371007e0008000000057800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_6', 'jobgroup_2', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000274000a7363686564756c6549647371007e0008000000067800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_7', 'jobgroup_2', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000274000a7363686564756c6549647371007e0008000000077800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_15', 'jobgroup_3', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000374000a7363686564756c6549647371007e00080000000f7800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_18', 'jobgroup_3', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000374000a7363686564756c6549647371007e0008000000127800');
INSERT INTO public.qrtz_job_details VALUES ('DolphinScheduler', 'job_17', 'jobgroup_3', NULL, 'org.apache.dolphinscheduler.scheduler.quartz.ProcessScheduleTask', false, false, false, false, '\xaced0005737200156f72672e71756172747a2e4a6f62446174614d61709fb083e8bfa9b0cb020000787200266f72672e71756172747a2e7574696c732e537472696e674b65794469727479466c61674d61708208e8c3fbc55d280200015a0013616c6c6f77735472616e7369656e74446174617872001d6f72672e71756172747a2e7574696c732e4469727479466c61674d617013e62ead28760ace0200025a000564697274794c00036d617074000f4c6a6176612f7574696c2f4d61703b787001737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000274000970726f6a6563744964737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000374000a7363686564756c6549647371007e0008000000117800');


--
-- Data for Name: qrtz_locks; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qrtz_locks VALUES ('DolphinScheduler', 'STATE_ACCESS');
INSERT INTO public.qrtz_locks VALUES ('DolphinScheduler', 'TRIGGER_ACCESS');


--
-- Data for Name: qrtz_paused_trigger_grps; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_scheduler_state; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qrtz_scheduler_state VALUES ('DolphinScheduler', '0e4d3778eb761759196054726', 1759221595357, 5000);


--
-- Data for Name: qrtz_simple_triggers; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_simprop_triggers; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: qrtz_triggers; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_1', 'jobgroup_3', 'job_1', 'jobgroup_3', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1756874378000, 4910474378000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_2', 'jobgroup_2', 'job_2', 'jobgroup_2', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758014906000, 4911614906000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_3', 'jobgroup_3', 'job_3', 'jobgroup_3', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758014992000, 4911614992000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_4', 'jobgroup_2', 'job_4', 'jobgroup_2', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758271769000, 4911871769000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_5', 'jobgroup_2', 'job_5', 'jobgroup_2', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758271770000, 4911871770000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_6', 'jobgroup_2', 'job_6', 'jobgroup_2', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758271772000, 4911871772000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_7', 'jobgroup_2', 'job_7', 'jobgroup_2', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1758271796000, 4911871796000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_15', 'jobgroup_3', 'job_15', 'jobgroup_3', NULL, 1759248000000, 1759161600000, 5, 'WAITING', 'CRON', 1758528445000, 4912128445000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_18', 'jobgroup_3', 'job_18', 'jobgroup_3', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1759217435000, 4912817435000, NULL, -1, '\x');
INSERT INTO public.qrtz_triggers VALUES ('DolphinScheduler', 'job_17', 'jobgroup_3', 'job_17', 'jobgroup_3', NULL, 1759248000000, -1, 5, 'WAITING', 'CRON', 1759217760000, 4912817760000, NULL, -1, '\x');


--
-- Data for Name: t_ds_access_token; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_access_token VALUES (1, 1, 'b07f0e57c4818043a57ba05a28da291a', '2035-03-31 14:22:00', '2025-03-04 14:22:08.972', '2025-03-04 15:25:41.425');


--
-- Data for Name: t_ds_alert; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_alert VALUES (1, 'Fault tolerance warning', '3eccab5d189f1b440160f75e0fdd755e61c71716', '[{"type":"MASTER","host":"/nodes/master/172.18.0.14:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 11:38:41.935', '2025-09-02 11:38:47.293', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (2, 'Fault tolerance warning', 'f95a2209531008c6b69fbe3c8489f93178f00c03', '[{"type":"WORKER","host":"/nodes/worker/172.18.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 11:38:41.935', '2025-09-02 11:38:47.293', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (35, 'Fault tolerance warning', '0f7cc519f1a3903abf0aa0009ffd4d240b933618', '[{"type":"MASTER","host":"/nodes/master/172.19.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 15:46:09.802', '2025-09-02 15:46:15.027', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (34, 'Fault tolerance warning', 'c7a384c5d6351b650e6c643d0a7964d7826c1a3d', '[{"type":"WORKER","host":"/nodes/worker/172.19.0.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 15:46:09.802', '2025-09-02 15:46:15.027', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (36, 'Fault tolerance warning', '1a60fb74a859d1ee10f361f4a81241b88073c65f', '[{"type":"WORKER","host":"/nodes/worker/172.22.0.14:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:10:38.886', '2025-09-02 16:10:44.227', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (37, 'Fault tolerance warning', 'f1a205d49622fb570fdd4adea4817dadcfcc87cc', '[{"type":"MASTER","host":"/nodes/master/172.22.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:10:38.886', '2025-09-02 16:10:44.227', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (69, 'Fault tolerance warning', '13dc41dc6cc10b74abc8be4f0e891806e887a65c', '[{"type":"WORKER","host":"/nodes/worker/172.23.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:22:34.148', '2025-09-02 16:22:42.757', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (70, 'Fault tolerance warning', 'df4459cdcad4b6025d50ff779c69857ad71f81e4', '[{"type":"MASTER","host":"/nodes/master/172.23.0.14:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:22:34.148', '2025-09-02 16:22:42.759', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (103, 'Fault tolerance warning', '583dc18fd3830b3c8a0bd83291237762c4e441b3', '[{"type":"WORKER","host":"/nodes/worker/172.25.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:50:25.656', '2025-09-02 16:50:29.552', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (102, 'Fault tolerance warning', 'a4f4568f68c980318c14016abba8cec289147bde', '[{"type":"MASTER","host":"/nodes/master/172.25.0.14:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:50:25.655', '2025-09-02 16:50:29.553', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (136, 'Fault tolerance warning', 'a14cacf92d0b86f8762d445704e3bb494ae6140d', '[{"type":"WORKER","host":"/nodes/worker/172.26.0.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:58:43.414', '2025-09-02 16:58:47.324', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (135, 'Fault tolerance warning', 'b06bdf9487197e93b62182c3aef458b8c18344cd', '[{"type":"MASTER","host":"/nodes/master/172.26.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 16:58:43.414', '2025-09-02 16:58:47.323', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (138, 'Fault tolerance warning', 'f7f276b573b4be09a1689d664ddfda13e33315ce', '[{"type":"MASTER","host":"/nodes/master/172.27.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 17:07:38.352', '2025-09-02 17:07:44.346', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (137, 'Fault tolerance warning', '0cb8acd7da815544cdb7ba96532f75edac218185', '[{"type":"WORKER","host":"/nodes/worker/172.27.0.14:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 17:07:38.352', '2025-09-02 17:07:44.346', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (170, 'Fault tolerance warning', 'e46e09e4a9c4d56a5596a4a79ab61085ad690f00', '[{"type":"MASTER","host":"/nodes/master/172.28.0.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 17:19:39.079', '2025-09-02 17:19:42.547', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (171, 'Fault tolerance warning', '0cce2d20775a05686f201e031a373c1a5db4d74f', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-02 17:19:39.079', '2025-09-02 17:19:42.547', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (172, 'Fault tolerance warning', '13a04d2fbf7ca9e70563a781030d5f5a3c50e3c0', '[{"type":"WORKER","host":"/nodes/worker/172.22.0.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 12:07:25.198', '2025-09-03 12:07:29.947', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (173, 'Fault tolerance warning', '1cea91eb42d4653d431e6b8c28db495c7f8645bb', '[{"type":"MASTER","host":"/nodes/master/172.22.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 12:07:25.198', '2025-09-03 12:07:29.947', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (174, 'Fault tolerance warning', '4b0f2e0154c6467d4d246f79ae1e771cee40639d', '[{"type":"WORKER","host":"/nodes/worker/172.26.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 14:57:51.805', '2025-09-03 14:57:54.998', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (175, 'Fault tolerance warning', '1ebc2ce76a091b9454c0740d0678799664d5d22a', '[{"type":"MASTER","host":"/nodes/master/172.26.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 14:57:51.805', '2025-09-03 14:57:54.998', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (177, 'Fault tolerance warning', '9cf4af557beaa6fe3aaf22e81c652474af215b9e', '[{"type":"MASTER","host":"/nodes/master/172.28.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:05:07.377', '2025-09-03 17:05:10.877', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (176, 'Fault tolerance warning', '498d044b5a37ddbed49af75922e87dd9ba574f6b', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.7:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:05:07.377', '2025-09-03 17:05:10.878', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (209, 'Fault tolerance warning', '16aac7090385b2396b89a0282771b1eac401d32c', '[{"type":"WORKER","host":"/nodes/worker/172.29.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:22:13.516', '2025-09-03 17:22:16.5', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (210, 'Fault tolerance warning', 'dcbc5fa9063199bd901a561d7eb27002ad56f521', '[{"type":"MASTER","host":"/nodes/master/172.29.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:22:13.516', '2025-09-03 17:22:16.5', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (242, 'Fault tolerance warning', 'd48ac780002b72e3342b6c2c432ee4116bc072c5', '[{"type":"WORKER","host":"/nodes/worker/172.30.0.14:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:36:43.931', '2025-09-03 17:36:50.237', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (243, 'Fault tolerance warning', '25ddaa2e170c318b91c6bd54ce10dfa5648a56c6', '[{"type":"MASTER","host":"/nodes/master/172.30.0.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-03 17:36:43.931', '2025-09-03 17:36:50.237', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (244, 'Fault tolerance warning', 'b3449845c2ae3c7d50ea964f001abb375e241bde', '[{"type":"MASTER","host":"/nodes/master/172.31.0.14:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 13:58:26.905', '2025-09-09 13:58:32.367', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (245, 'Fault tolerance warning', 'eb057ae22dd684bb01034ab377eee5dbebc4a0b6', '[{"type":"WORKER","host":"/nodes/worker/172.31.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 13:58:26.905', '2025-09-09 13:58:32.367', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (247, 'Fault tolerance warning', '39878c16542b4f0f9baf5f3ffc9b0388d0a54e74', '[{"type":"WORKER","host":"/nodes/worker/192.168.160.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:31:43.633', '2025-09-09 14:31:50.677', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (246, 'Fault tolerance warning', '73217a080e2bf4d41771c64891f7b1d2b33db04b', '[{"type":"MASTER","host":"/nodes/master/192.168.160.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:31:43.633', '2025-09-09 14:31:50.677', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (279, 'Fault tolerance warning', '3c3b4f6bc98623c26e4a1cc0d12981e5fbbe45c0', '[{"type":"WORKER","host":"/nodes/worker/172.19.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:34:52.03', '2025-09-09 14:34:57.631', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (280, 'Fault tolerance warning', 'aee9a3c613fb64d6050287bb63d4997cc928dc10', '[{"type":"MASTER","host":"/nodes/master/172.19.0.14:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:34:52.03', '2025-09-09 14:34:57.631', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (282, 'Fault tolerance warning', '13dc41dc6cc10b74abc8be4f0e891806e887a65c', '[{"type":"WORKER","host":"/nodes/worker/172.23.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:59:29.779', '2025-09-09 14:59:37.865', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (281, 'Fault tolerance warning', '485b5dc3c4872d9d8c9b60b497f0ba9ed5222acb', '[{"type":"MASTER","host":"/nodes/master/172.23.0.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-09 14:59:29.779', '2025-09-09 14:59:37.854', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (315, 'Fault tolerance warning', '764a9347c10d52eee0efc990d67e74646e107668', '[{"type":"MASTER","host":"/nodes/master/172.25.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-11 09:10:21.452', '2025-09-11 09:10:27.194', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (314, 'Fault tolerance warning', '4190acc3b4f24e86b83289d50d200c0d3ce20df1', '[{"type":"WORKER","host":"/nodes/worker/172.25.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-11 09:10:21.452', '2025-09-11 09:10:27.194', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (316, 'Fault tolerance warning', '735fab345198d37bd1726c3e8cba50f38af3a8de', '[{"type":"MASTER","host":"/nodes/master/172.26.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 16:58:51.98', '2025-09-16 16:58:55.574', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (317, 'Fault tolerance warning', '6b720d0a7553f08dd91e15bbd4a183df51ee16a1', '[{"type":"WORKER","host":"/nodes/worker/172.26.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 16:58:51.98', '2025-09-16 16:58:55.573', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (318, 'Fault tolerance warning', '3ab962b730edee1b7b1ada98c29e95d64cf17566', '[{"type":"MASTER","host":"/nodes/master/172.24.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:20:13.988', '2025-09-16 17:20:20.466', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (319, 'Fault tolerance warning', '6ed9dc512a743b7b18ec2ad2fead37ce3a63782a', '[{"type":"WORKER","host":"/nodes/worker/172.24.0.18:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:20:13.988', '2025-09-16 17:20:20.466', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (320, 'Fault tolerance warning', '3a0d6b3b73c0b1ec72d32ef05d443cbc1654503f', '[{"type":"WORKER","host":"/nodes/worker/172.24.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:43:37.987', '2025-09-16 17:43:41.936', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (321, 'Fault tolerance warning', 'b9a79bb289f58eac736c5f63d4c73f87c799d43c', '[{"type":"MASTER","host":"/nodes/master/172.24.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:43:37.987', '2025-09-16 17:43:41.938', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (354, 'Fault tolerance warning', '764a9347c10d52eee0efc990d67e74646e107668', '[{"type":"MASTER","host":"/nodes/master/172.25.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:46:47.985', '2025-09-16 17:46:52.918', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (353, 'Fault tolerance warning', '583dc18fd3830b3c8a0bd83291237762c4e441b3', '[{"type":"WORKER","host":"/nodes/worker/172.25.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:46:47.985', '2025-09-16 17:46:52.916', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (386, 'Fault tolerance warning', '735fab345198d37bd1726c3e8cba50f38af3a8de', '[{"type":"MASTER","host":"/nodes/master/172.26.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:50:35.985', '2025-09-16 17:50:39.295', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (387, 'Fault tolerance warning', '4b0f2e0154c6467d4d246f79ae1e771cee40639d', '[{"type":"WORKER","host":"/nodes/worker/172.26.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 17:50:35.985', '2025-09-16 17:50:39.296', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (419, 'Fault tolerance warning', 'b67256e001ba381554a8628aee6ce62b21c4e4f4', '[{"type":"MASTER","host":"/nodes/master/172.27.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 18:01:19.98', '2025-09-16 18:01:25.516', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (420, 'Fault tolerance warning', '39eaf008446e0461c336b42ee934d16d410a8533', '[{"type":"WORKER","host":"/nodes/worker/172.27.0.18:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 18:01:19.98', '2025-09-16 18:01:25.518', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (421, 'Fault tolerance warning', '9cf4af557beaa6fe3aaf22e81c652474af215b9e', '[{"type":"MASTER","host":"/nodes/master/172.28.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 18:09:09.986', '2025-09-16 18:09:15.225', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (422, 'Fault tolerance warning', '0cce2d20775a05686f201e031a373c1a5db4d74f', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-16 18:09:09.986', '2025-09-16 18:09:15.226', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (455, 'Fault tolerance warning', '16aac7090385b2396b89a0282771b1eac401d32c', '[{"type":"WORKER","host":"/nodes/worker/172.29.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-17 10:02:21.094', '2025-09-17 10:02:25.299', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (454, 'Fault tolerance warning', '27caa5b29d61667876d423375e92ad7bc667e780', '[{"type":"MASTER","host":"/nodes/master/172.29.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-17 10:02:21.094', '2025-09-17 10:02:25.299', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (487, 'Fault tolerance warning', '64874ea84bbd80a10788e10f87e1543231fd3381', '[{"type":"MASTER","host":"/nodes/master/172.31.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-17 10:05:23.867', '2025-09-17 10:05:30.828', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (488, 'Fault tolerance warning', 'eb057ae22dd684bb01034ab377eee5dbebc4a0b6', '[{"type":"WORKER","host":"/nodes/worker/172.31.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-17 10:05:23.867', '2025-09-17 10:05:30.828', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (490, 'Fault tolerance warning', '69c6f162b554bbbb9c5f563b451c31af35306a73', '[{"type":"MASTER","host":"/nodes/master/192.168.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-18 09:52:45.017', '2025-09-18 09:52:49.548', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (489, 'Fault tolerance warning', 'bf45300c03ec97dcbdc1ad2e5ea189218fc2e95d', '[{"type":"WORKER","host":"/nodes/worker/192.168.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-18 09:52:45.017', '2025-09-18 09:52:49.547', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (492, 'Fault tolerance warning', '5a42db804e29e4ecfefc5812e944afb5423f7c7e', '[{"type":"MASTER","host":"/nodes/master/192.168.16.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-18 10:28:52.147', '2025-09-18 10:28:56.626', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (491, 'Fault tolerance warning', 'aa616a2eaf14faef8e048e5cb8626afcdd982555', '[{"type":"WORKER","host":"/nodes/worker/192.168.16.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-18 10:28:52.147', '2025-09-18 10:28:56.625', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (493, 'Fault tolerance warning', 'b23c3923412d6686c5059a9cc297060f469977be', '[{"type":"WORKER","host":"/nodes/worker/192.168.32.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:15:27.127', '2025-09-19 14:15:32.119', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (494, 'Fault tolerance warning', 'ad4040c1bc500dbb8a557f714853cc0c4f371851', '[{"type":"MASTER","host":"/nodes/master/192.168.32.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:15:27.127', '2025-09-19 14:15:32.12', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (495, 'Fault tolerance warning', '113d61206913cacffd09a34f577204380251dc95', '[{"type":"WORKER","host":"/nodes/worker/172.18.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:22:32.114', '2025-09-19 14:22:36.358', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (496, 'Fault tolerance warning', '6b2e9520842b600e1b1be009179985eb6c853e0a', '[{"type":"MASTER","host":"/nodes/master/172.18.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:22:32.114', '2025-09-19 14:22:36.359', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (497, 'Fault tolerance warning', '0a503d4a7cbf28ddb4bcd2dadaee7b364b135c3e', '[{"type":"MASTER","host":"/nodes/master/172.19.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:41:14.176', '2025-09-19 14:41:20.729', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (498, 'Fault tolerance warning', '3c3b4f6bc98623c26e4a1cc0d12981e5fbbe45c0', '[{"type":"WORKER","host":"/nodes/worker/172.19.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:41:14.176', '2025-09-19 14:41:20.73', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (499, 'Fault tolerance warning', 'ee691de36ab7401f37644520407516e4ebe283db', '[{"type":"MASTER","host":"/nodes/master/172.28.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:51:08.159', '2025-09-19 14:51:12.063', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (500, 'Fault tolerance warning', '1c2b81b1abb5915f43d15881173833f35f702dd4', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 14:51:08.159', '2025-09-19 14:51:12.064', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (502, 'Fault tolerance warning', 'e46e09e4a9c4d56a5596a4a79ab61085ad690f00', '[{"type":"MASTER","host":"/nodes/master/172.28.0.17:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 15:01:38.97', '2025-09-19 15:01:46.019', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (501, 'Fault tolerance warning', 'c4563a892f43c78ac3067c8fcc0bb6a1d2f37e8f', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.6:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-19 15:01:38.97', '2025-09-19 15:01:46.019', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (503, 'Fault tolerance warning', 'ee691de36ab7401f37644520407516e4ebe283db', '[{"type":"MASTER","host":"/nodes/master/172.28.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-26 13:55:50.786', '2025-09-26 13:55:57.106', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (504, 'Fault tolerance warning', '4dbe2fba6d707d9b424ccc6ec8e7cae030f83fff', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.18:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-26 13:55:50.783', '2025-09-26 13:55:57.106', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (505, 'Fault tolerance warning', '6b2e9520842b600e1b1be009179985eb6c853e0a', '[{"type":"MASTER","host":"/nodes/master/172.18.0.15:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-26 14:19:40.993', '2025-09-26 14:19:46.253', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (506, 'Fault tolerance warning', '8384daee422961b4596e6ce675e68a752a9bf142', '[{"type":"WORKER","host":"/nodes/worker/172.18.0.13:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-26 14:19:40.993', '2025-09-26 14:19:46.254', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (508, 'Fault tolerance warning', '9591ed59c36a24439d11b9997ce422ae3558f380', '[{"type":"MASTER","host":"/nodes/master/172.28.0.6:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-28 11:06:14.998', '2025-09-28 11:06:19.595', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (507, 'Fault tolerance warning', '0cce2d20775a05686f201e031a373c1a5db4d74f', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.16:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-28 11:06:09.445', '2025-09-28 11:06:19.595', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (509, 'Fault tolerance warning', 'ee691de36ab7401f37644520407516e4ebe283db', '[{"type":"MASTER","host":"/nodes/master/172.28.0.16:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-28 18:21:34.164', '2025-09-28 18:21:46.887', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (510, 'Fault tolerance warning', '1c2b81b1abb5915f43d15881173833f35f702dd4', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.15:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-28 18:21:34.501', '2025-09-28 18:21:46.888', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (542, 'Fault tolerance warning', 'a2e3f603689e617f0ba7f24b3a0fef0f50d7c11d', '[{"type":"WORKER","host":"/nodes/worker/172.28.0.17:1234","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-30 09:34:15.922', '2025-09-30 09:34:25.379', NULL, NULL, NULL, 4);
INSERT INTO public.t_ds_alert VALUES (543, 'Fault tolerance warning', '2c95566b3433d2af0cc514942882ea8ac1d3b3ba', '[{"type":"MASTER","host":"/nodes/master/172.28.0.18:5678","event":"SERVER_DOWN","warningLevel":"SERIOUS"}]', 2, 2, 'No bind plugin instance found', 1, '2025-09-30 09:34:20.296', '2025-09-30 09:34:25.379', NULL, NULL, NULL, 4);


--
-- Data for Name: t_ds_alert_plugin_instance; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_alert_send_status; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_alertgroup; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_alertgroup VALUES (1, NULL, 1, 'default admin warning group', 'default admin warning group', '2018-11-29 10:20:39', '2018-11-29 10:20:39');
INSERT INTO public.t_ds_alertgroup VALUES (2, NULL, 1, 'global alert group', 'global alert group', '2018-11-29 10:20:39', '2018-11-29 10:20:39');


--
-- Data for Name: t_ds_audit_log; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_audit_log VALUES (1, 1, 150906647848512, '测试', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 126, NULL, '2025-09-02 16:02:55.828');
INSERT INTO public.t_ds_audit_log VALUES (2, 1, 150906647848512, '测试', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 452, NULL, '2025-09-02 16:03:25.507');
INSERT INTO public.t_ds_audit_log VALUES (3, 1, 150911519707360, '测试111', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 195, NULL, '2025-09-02 17:26:47.985');
INSERT INTO public.t_ds_audit_log VALUES (4, 1, 150911519707360, '测试111', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 36, NULL, '2025-09-02 17:26:52.082');
INSERT INTO public.t_ds_audit_log VALUES (5, 1, 150911519707360, '测试111', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 68, 'latest', '2025-09-02 17:26:54.068');
INSERT INTO public.t_ds_audit_log VALUES (6, 1, 150911519707360, '测试111', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 19, NULL, '2025-09-02 17:29:20.19');
INSERT INTO public.t_ds_audit_log VALUES (7, 1, 150911519707360, '测试111', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 56, NULL, '2025-09-02 17:31:15.627');
INSERT INTO public.t_ds_audit_log VALUES (8, 1, 150911519707360, '测试111', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-02 17:31:22.871');
INSERT INTO public.t_ds_audit_log VALUES (9, 1, 150911519707360, '测试111', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-02 17:31:25.549');
INSERT INTO public.t_ds_audit_log VALUES (10, 1, 150911519707360, '测试111', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-02 17:35:29.244');
INSERT INTO public.t_ds_audit_log VALUES (11, 1, 150981006147328, '测试', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 157, NULL, '2025-09-03 12:13:08.711');
INSERT INTO public.t_ds_audit_log VALUES (12, 1, 150981006147328, '测试', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 18, NULL, '2025-09-03 12:13:41.021');
INSERT INTO public.t_ds_audit_log VALUES (13, 1, 150981006147328, '测试', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 40, 'latest', '2025-09-03 12:13:43.95');
INSERT INTO public.t_ds_audit_log VALUES (14, 1, 150981006147328, '测试', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-03 12:14:55.188');
INSERT INTO public.t_ds_audit_log VALUES (15, 1, 150981006147328, '测试', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 141, NULL, '2025-09-03 12:16:14.338');
INSERT INTO public.t_ds_audit_log VALUES (16, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 47, NULL, '2025-09-03 12:39:08.835');
INSERT INTO public.t_ds_audit_log VALUES (17, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-03 12:39:08.899');
INSERT INTO public.t_ds_audit_log VALUES (18, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 132, '1', '2025-09-03 12:39:08.97');
INSERT INTO public.t_ds_audit_log VALUES (19, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Schedule', 'Online', 'ONLINE_SCHEDULE_NOTES', 135, '1', '2025-09-03 12:39:09.124');
INSERT INTO public.t_ds_audit_log VALUES (20, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-03 12:39:12.543');
INSERT INTO public.t_ds_audit_log VALUES (21, 1, 150982603138816, '测试JEoZH8A1756874348816', 'Schedule', 'Offline', 'OFFLINE_SCHEDULE_NOTES', 3, '1', '2025-09-03 12:39:12.58');
INSERT INTO public.t_ds_audit_log VALUES (22, 1, 150982603138816, '测试9LMXA6X1756874378318', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 65, NULL, '2025-09-03 12:39:38.342');
INSERT INTO public.t_ds_audit_log VALUES (23, 1, 150982603138816, '测试9LMXA6X1756874378318', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-03 12:39:38.421');
INSERT INTO public.t_ds_audit_log VALUES (24, 1, 150982603138816, '测试9LMXA6X1756874378318', 'Schedule', 'Update', 'UPDATE_SCHEDULE_NOTES', 17, '1', '2025-09-03 12:39:38.452');
INSERT INTO public.t_ds_audit_log VALUES (25, 1, 150982603138816, '测试9LMXA6X1756874378318', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-03 12:39:42.109');
INSERT INTO public.t_ds_audit_log VALUES (26, 1, 151522243874464, '122121', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 228, NULL, '2025-09-09 15:02:21.125');
INSERT INTO public.t_ds_audit_log VALUES (27, 1, 151522243874464, '122121', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 39, NULL, '2025-09-09 15:02:26.722');
INSERT INTO public.t_ds_audit_log VALUES (28, 1, 151522243874464, '122121', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-09 15:02:30.446');
INSERT INTO public.t_ds_audit_log VALUES (29, 1, 151522243874464, '122121', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 441, NULL, '2025-09-09 15:02:32.411');
INSERT INTO public.t_ds_audit_log VALUES (59, 1, 151678330518176, '测试', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 303, NULL, '2025-09-11 09:22:53.439');
INSERT INTO public.t_ds_audit_log VALUES (60, 1, 151678330518176, '测试', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 51, NULL, '2025-09-11 09:22:59.819');
INSERT INTO public.t_ds_audit_log VALUES (61, 1, 151678330518176, '测试', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 81, 'latest', '2025-09-11 09:23:00.84');
INSERT INTO public.t_ds_audit_log VALUES (62, 1, 151678330518176, '测试', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-11 09:23:41.493');
INSERT INTO public.t_ds_audit_log VALUES (63, 1, 151678330518176, '测试', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 71, NULL, '2025-09-11 09:24:14.903');
INSERT INTO public.t_ds_audit_log VALUES (64, 1, 151678330518176, '测试', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-11 09:24:20.339');
INSERT INTO public.t_ds_audit_log VALUES (65, 1, 151678330518176, '测试', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-11 09:24:21.521');
INSERT INTO public.t_ds_audit_log VALUES (66, 1, 6, '测试-1-20250911092301573', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 100, NULL, '2025-09-11 09:24:34.073');
INSERT INTO public.t_ds_audit_log VALUES (67, 1, 3, '测试111-2-20250902173529867', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 100, NULL, '2025-09-11 09:24:34.073');
INSERT INTO public.t_ds_audit_log VALUES (68, 1, 2, '测试111-2-20250902173126130', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 100, NULL, '2025-09-11 09:24:34.073');
INSERT INTO public.t_ds_audit_log VALUES (69, 1, 1, '测试111-1-20250902172655113', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 100, NULL, '2025-09-11 09:24:34.073');
INSERT INTO public.t_ds_audit_log VALUES (70, 1, 151678558711456, '测试2', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-11 09:26:35.221');
INSERT INTO public.t_ds_audit_log VALUES (71, 1, 151678558711456, '测试2', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-11 09:26:40.255');
INSERT INTO public.t_ds_audit_log VALUES (72, 1, 151678558711456, '测试2', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-11 09:26:42.179');
INSERT INTO public.t_ds_audit_log VALUES (73, 1, -1, 'file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar', 'File', 'Delete', 'DELETE_RESOURCE_BY_ID_NOTES', 75, NULL, '2025-09-11 10:01:12.154');
INSERT INTO public.t_ds_audit_log VALUES (74, 1, -1, 'qdata-etl-3.8.8.jar', 'File', 'Create', 'CREATE_RESOURCE_NOTES', 13190, NULL, '2025-09-11 10:01:31.326');
INSERT INTO public.t_ds_audit_log VALUES (75, 1, 151678558711456, '测试2', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-11 10:01:51.252');
INSERT INTO public.t_ds_audit_log VALUES (76, 1, -1, 'file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar', 'File', 'Delete', 'DELETE_RESOURCE_BY_ID_NOTES', 82, NULL, '2025-09-11 10:05:21.069');
INSERT INTO public.t_ds_audit_log VALUES (77, 1, -1, 'qdata-etl-3.8.8.jar', 'File', 'Create', 'CREATE_RESOURCE_NOTES', 15282, NULL, '2025-09-11 10:05:30.848');
INSERT INTO public.t_ds_audit_log VALUES (78, 1, 151678558711456, '测试2', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 7, 'latest', '2025-09-11 10:05:52.777');
INSERT INTO public.t_ds_audit_log VALUES (79, 1, 151678558711456, '测试2', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-11 10:07:01.356');
INSERT INTO public.t_ds_audit_log VALUES (80, 1, 151678558711456, '测试2', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-11 10:12:06.199');
INSERT INTO public.t_ds_audit_log VALUES (81, 1, 151678558711456, '测试2', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 107, NULL, '2025-09-11 10:12:16.273');
INSERT INTO public.t_ds_audit_log VALUES (82, 1, 151678330518176, '测试', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-11 10:12:23.933');
INSERT INTO public.t_ds_audit_log VALUES (83, 1, 151678330518176, '测试', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 28, NULL, '2025-09-11 10:12:26.558');
INSERT INTO public.t_ds_audit_log VALUES (84, 1, 151682751874720, '测呃呃呃', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 25, NULL, '2025-09-11 10:36:16.306');
INSERT INTO public.t_ds_audit_log VALUES (85, 1, 151682751874720, '测呃呃呃', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 25, NULL, '2025-09-11 10:36:37.733');
INSERT INTO public.t_ds_audit_log VALUES (86, 1, 152149740847040, 'ces ', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 308, NULL, '2025-09-16 17:15:31.068');
INSERT INTO public.t_ds_audit_log VALUES (87, 1, 152149740847040, 'ces ', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 50, NULL, '2025-09-16 17:15:36.978');
INSERT INTO public.t_ds_audit_log VALUES (88, 1, 152149740847040, 'ces ', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 91, 'latest', '2025-09-16 17:15:51.057');
INSERT INTO public.t_ds_audit_log VALUES (89, 1, 152149740847040, 'ces ', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-16 17:19:21.488');
INSERT INTO public.t_ds_audit_log VALUES (90, 1, 152149740847040, 'ces ', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 82, NULL, '2025-09-16 17:19:23.938');
INSERT INTO public.t_ds_audit_log VALUES (122, 1, 152150115280832, 'ces', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 133, NULL, '2025-09-16 17:21:36.864');
INSERT INTO public.t_ds_audit_log VALUES (123, 1, 152150115280832, 'ces', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 24, NULL, '2025-09-16 17:21:41.362');
INSERT INTO public.t_ds_audit_log VALUES (124, 1, 152150115280832, 'ces', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 52, 'latest', '2025-09-16 17:21:45.16');
INSERT INTO public.t_ds_audit_log VALUES (125, 1, 152150115280832, 'ces', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 17, NULL, '2025-09-16 17:23:27.292');
INSERT INTO public.t_ds_audit_log VALUES (126, 1, 152150115280832, 'ces', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 84, NULL, '2025-09-16 17:23:30.605');
INSERT INTO public.t_ds_audit_log VALUES (127, 1, 152150248157120, '232332', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 42, NULL, '2025-09-16 17:24:34.324');
INSERT INTO public.t_ds_audit_log VALUES (128, 1, 152150248157120, '232332', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-16 17:24:38.153');
INSERT INTO public.t_ds_audit_log VALUES (129, 1, 152150248157120, '232332', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-16 17:24:39.36');
INSERT INTO public.t_ds_audit_log VALUES (130, 1, 152150248157120, '232332', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-16 17:25:28.175');
INSERT INTO public.t_ds_audit_log VALUES (131, 1, 152150248157120, '232332', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-16 17:27:03.171');
INSERT INTO public.t_ds_audit_log VALUES (132, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 24, NULL, '2025-09-16 17:28:10.795');
INSERT INTO public.t_ds_audit_log VALUES (133, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-16 17:28:10.831');
INSERT INTO public.t_ds_audit_log VALUES (134, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 52, '2', '2025-09-16 17:28:10.888');
INSERT INTO public.t_ds_audit_log VALUES (135, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Schedule', 'Online', 'ONLINE_SCHEDULE_NOTES', 80, '2', '2025-09-16 17:28:10.965');
INSERT INTO public.t_ds_audit_log VALUES (136, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 18, NULL, '2025-09-16 17:28:14.121');
INSERT INTO public.t_ds_audit_log VALUES (137, 1, 152150518585280, '232332JLZSD3A1758014890777', 'Schedule', 'Offline', 'OFFLINE_SCHEDULE_NOTES', 2, '2', '2025-09-16 17:28:14.147');
INSERT INTO public.t_ds_audit_log VALUES (138, 1, 152150518585280, '232332Cjy7WIv1758014906806', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 37, NULL, '2025-09-16 17:28:26.826');
INSERT INTO public.t_ds_audit_log VALUES (139, 1, 152150518585280, '232332Cjy7WIv1758014906806', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-16 17:28:26.875');
INSERT INTO public.t_ds_audit_log VALUES (140, 1, 152150518585280, '232332Cjy7WIv1758014906806', 'Schedule', 'Update', 'UPDATE_SCHEDULE_NOTES', 9, '2', '2025-09-16 17:28:26.907');
INSERT INTO public.t_ds_audit_log VALUES (141, 1, 152150518585280, '232332Cjy7WIv1758014906806', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-16 17:28:30.837');
INSERT INTO public.t_ds_audit_log VALUES (142, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-16 17:29:52.754');
INSERT INTO public.t_ds_audit_log VALUES (143, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-16 17:29:52.774');
INSERT INTO public.t_ds_audit_log VALUES (144, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 7, '3', '2025-09-16 17:29:52.788');
INSERT INTO public.t_ds_audit_log VALUES (145, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-16 17:29:54.06');
INSERT INTO public.t_ds_audit_log VALUES (146, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 7, 'latest', '2025-09-16 17:29:56.482');
INSERT INTO public.t_ds_audit_log VALUES (147, 1, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-16 17:30:03.722');
INSERT INTO public.t_ds_audit_log VALUES (148, 1, 152150699767744, '1212', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-16 17:31:07.741');
INSERT INTO public.t_ds_audit_log VALUES (149, 1, 152150699767744, '1212', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-16 17:31:49.204');
INSERT INTO public.t_ds_audit_log VALUES (150, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 346, NULL, '2025-09-19 16:46:32.491');
INSERT INTO public.t_ds_audit_log VALUES (151, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 46, NULL, '2025-09-19 16:46:32.99');
INSERT INTO public.t_ds_audit_log VALUES (152, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 109, '4', '2025-09-19 16:46:33.063');
INSERT INTO public.t_ds_audit_log VALUES (153, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Schedule', 'Online', 'ONLINE_SCHEDULE_NOTES', 108, '4', '2025-09-19 16:46:33.192');
INSERT INTO public.t_ds_audit_log VALUES (154, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 71, 'latest', '2025-09-19 16:46:33.656');
INSERT INTO public.t_ds_audit_log VALUES (155, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-19 16:47:10.875');
INSERT INTO public.t_ds_audit_log VALUES (156, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Schedule', 'Offline', 'OFFLINE_SCHEDULE_NOTES', 3, '4', '2025-09-19 16:47:10.911');
INSERT INTO public.t_ds_audit_log VALUES (157, 1, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 'Schedule', 'Update', 'UPDATE_SCHEDULE_NOTES', 12, '4', '2025-09-19 16:47:32.799');
INSERT INTO public.t_ds_audit_log VALUES (158, 1, 152413381367104, '工程监管数据发现wSAxLkg1758271769213', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 44, NULL, '2025-09-19 16:49:29.227');
INSERT INTO public.t_ds_audit_log VALUES (159, 1, 152413381367104, '工程监管数据发现wSAxLkg1758271769213', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-19 16:49:29.282');
INSERT INTO public.t_ds_audit_log VALUES (160, 1, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-19 16:49:30.696');
INSERT INTO public.t_ds_audit_log VALUES (161, 1, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-19 16:49:30.724');
INSERT INTO public.t_ds_audit_log VALUES (162, 1, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 10, '5', '2025-09-19 16:49:30.742');
INSERT INTO public.t_ds_audit_log VALUES (163, 1, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-19 16:49:32.322');
INSERT INTO public.t_ds_audit_log VALUES (164, 1, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-19 16:49:32.357');
INSERT INTO public.t_ds_audit_log VALUES (165, 1, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 8, '6', '2025-09-19 16:49:32.376');
INSERT INTO public.t_ds_audit_log VALUES (166, 1, 152413381367104, '工程监管数据发现wSAxLkg1758271769213', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-19 16:49:36.824');
INSERT INTO public.t_ds_audit_log VALUES (167, 1, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-19 16:49:39.754');
INSERT INTO public.t_ds_audit_log VALUES (168, 1, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-19 16:49:42.187');
INSERT INTO public.t_ds_audit_log VALUES (169, 1, 152413579812160, '水资源数据发现xAxpna41758271796919', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 14, NULL, '2025-09-19 16:49:56.927');
INSERT INTO public.t_ds_audit_log VALUES (170, 1, 152413579812160, '水资源数据发现xAxpna41758271796919', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-19 16:49:56.95');
INSERT INTO public.t_ds_audit_log VALUES (171, 1, 152413579812160, '水资源数据发现xAxpna41758271796919', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 7, '7', '2025-09-19 16:49:56.964');
INSERT INTO public.t_ds_audit_log VALUES (172, 1, 152413579812160, '水资源数据发现xAxpna41758271796919', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-19 16:49:58.454');
INSERT INTO public.t_ds_audit_log VALUES (173, 1, 152414754586944, '站点编码非空与格式稽查t0GWyOL1758273033134', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 61, NULL, '2025-09-19 17:10:33.177');
INSERT INTO public.t_ds_audit_log VALUES (174, 1, 152414754586944, '站点编码非空与格式稽查t0GWyOL1758273033134', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-19 17:10:33.253');
INSERT INTO public.t_ds_audit_log VALUES (175, 1, 152414754586944, '站点编码非空与格式稽查t0GWyOL1758273033134', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 14, '8', '2025-09-19 17:10:33.276');
INSERT INTO public.t_ds_audit_log VALUES (176, 1, 152414754586944, '站点编码非空与格式稽查t0GWyOL1758273033134', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 35, 'latest', '2025-09-19 17:10:35.407');
INSERT INTO public.t_ds_audit_log VALUES (177, 1, 152414792195392, '水位数值范围稽查Vgs9m9g1758273072566', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 17, NULL, '2025-09-19 17:11:12.579');
INSERT INTO public.t_ds_audit_log VALUES (178, 1, 152414792195392, '水位数值范围稽查Vgs9m9g1758273072566', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-19 17:11:12.606');
INSERT INTO public.t_ds_audit_log VALUES (709, 1, 152648927087936, '转化_水位行转列', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-30 15:39:28.999');
INSERT INTO public.t_ds_audit_log VALUES (179, 1, 152414792195392, '水位数值范围稽查Vgs9m9g1758273072566', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 7, '9', '2025-09-19 17:11:12.62');
INSERT INTO public.t_ds_audit_log VALUES (180, 1, 152414792195392, '水位数值范围稽查Vgs9m9g1758273072566', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-19 17:11:14.264');
INSERT INTO public.t_ds_audit_log VALUES (181, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 54, NULL, '2025-09-19 17:35:06.674');
INSERT INTO public.t_ds_audit_log VALUES (182, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 19, NULL, '2025-09-19 17:35:06.755');
INSERT INTO public.t_ds_audit_log VALUES (183, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 17, '10', '2025-09-19 17:35:06.785');
INSERT INTO public.t_ds_audit_log VALUES (184, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 18, NULL, '2025-09-19 17:35:15.469');
INSERT INTO public.t_ds_audit_log VALUES (185, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 25, NULL, '2025-09-19 17:35:17.821');
INSERT INTO public.t_ds_audit_log VALUES (186, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-19 17:35:17.855');
INSERT INTO public.t_ds_audit_log VALUES (187, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 18, 'latest', '2025-09-19 17:35:18.819');
INSERT INTO public.t_ds_audit_log VALUES (188, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-19 17:35:20.25');
INSERT INTO public.t_ds_audit_log VALUES (189, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-19 17:35:23.241');
INSERT INTO public.t_ds_audit_log VALUES (190, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 207, NULL, '2025-09-20 00:09:32.029');
INSERT INTO public.t_ds_audit_log VALUES (191, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-20 00:09:39.067');
INSERT INTO public.t_ds_audit_log VALUES (192, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 34, 'latest', '2025-09-20 00:09:41.47');
INSERT INTO public.t_ds_audit_log VALUES (193, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-20 00:12:58.423');
INSERT INTO public.t_ds_audit_log VALUES (194, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 38, NULL, '2025-09-20 00:13:15.016');
INSERT INTO public.t_ds_audit_log VALUES (195, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-20 00:13:18.45');
INSERT INTO public.t_ds_audit_log VALUES (196, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-20 00:13:20.419');
INSERT INTO public.t_ds_audit_log VALUES (197, 1, 30, '转化_水位预警等级校验-1-20250920000942062', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 53, NULL, '2025-09-20 00:13:28.496');
INSERT INTO public.t_ds_audit_log VALUES (198, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-20 00:19:37.058');
INSERT INTO public.t_ds_audit_log VALUES (199, 1, 31, '转化_水位预警等级校验-2-20250920001320612', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 9, NULL, '2025-09-20 00:19:44.175');
INSERT INTO public.t_ds_audit_log VALUES (200, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-20 00:21:39.042');
INSERT INTO public.t_ds_audit_log VALUES (201, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 31, NULL, '2025-09-20 00:21:50.365');
INSERT INTO public.t_ds_audit_log VALUES (202, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-20 00:21:53.024');
INSERT INTO public.t_ds_audit_log VALUES (203, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-20 00:21:54.652');
INSERT INTO public.t_ds_audit_log VALUES (204, 1, 33, '转化_水位预警等级校验-3-20250920002155418', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 90, NULL, '2025-09-20 00:22:07.117');
INSERT INTO public.t_ds_audit_log VALUES (205, 1, 33, '转化_水位预警等级校验-3-20250920002155418', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 21, NULL, '2025-09-20 00:22:15.359');
INSERT INTO public.t_ds_audit_log VALUES (206, 1, 32, '转化_水位预警等级校验-2-20250920001937184', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 21, NULL, '2025-09-20 00:22:15.359');
INSERT INTO public.t_ds_audit_log VALUES (207, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-20 00:22:18.613');
INSERT INTO public.t_ds_audit_log VALUES (208, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 52, NULL, '2025-09-20 00:22:23.596');
INSERT INTO public.t_ds_audit_log VALUES (209, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 30, NULL, '2025-09-20 00:22:31.397');
INSERT INTO public.t_ds_audit_log VALUES (210, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-20 00:22:34.303');
INSERT INTO public.t_ds_audit_log VALUES (211, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-20 00:22:36.086');
INSERT INTO public.t_ds_audit_log VALUES (212, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-20 00:23:43.239');
INSERT INTO public.t_ds_audit_log VALUES (213, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 24, NULL, '2025-09-20 00:23:50.704');
INSERT INTO public.t_ds_audit_log VALUES (214, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-20 00:23:53.603');
INSERT INTO public.t_ds_audit_log VALUES (215, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-20 00:24:00.843');
INSERT INTO public.t_ds_audit_log VALUES (216, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-20 00:26:26.678');
INSERT INTO public.t_ds_audit_log VALUES (217, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 42, NULL, '2025-09-22 09:02:28.095');
INSERT INTO public.t_ds_audit_log VALUES (218, 1, 152642396472640, '转化_水位列转行', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 159, NULL, '2025-09-22 09:25:52.277');
INSERT INTO public.t_ds_audit_log VALUES (219, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 46, NULL, '2025-09-22 09:26:30.249');
INSERT INTO public.t_ds_audit_log VALUES (220, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-22 09:26:40.924');
INSERT INTO public.t_ds_audit_log VALUES (221, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-22 09:26:58.942');
INSERT INTO public.t_ds_audit_log VALUES (222, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 33, NULL, '2025-09-22 09:27:17.46');
INSERT INTO public.t_ds_audit_log VALUES (223, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 09:27:21.009');
INSERT INTO public.t_ds_audit_log VALUES (224, 1, 152642396472640, '转化_水位列转行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 36, 'latest', '2025-09-22 09:27:29.363');
INSERT INTO public.t_ds_audit_log VALUES (225, 1, 35, '转化_水位预警等级校验-6-20250920002401709', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 38, NULL, '2025-09-22 09:27:42.572');
INSERT INTO public.t_ds_audit_log VALUES (226, 1, 34, '转化_水位预警等级校验-5-20250920002236520', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 38, NULL, '2025-09-22 09:27:42.572');
INSERT INTO public.t_ds_audit_log VALUES (227, 1, 152642396472640, '转化_水位列转行', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 09:29:37.694');
INSERT INTO public.t_ds_audit_log VALUES (228, 1, 152648200553792, '水位重复记录稽查JBmDEZ61758510409845', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 65, NULL, '2025-09-22 11:06:49.873');
INSERT INTO public.t_ds_audit_log VALUES (229, 1, 152648200553792, '水位重复记录稽查JBmDEZ61758510409845', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-22 11:06:49.957');
INSERT INTO public.t_ds_audit_log VALUES (231, 1, 152648200553792, '水位重复记录稽查JBmDEZ61758510409845', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 22, 'latest', '2025-09-22 11:06:56.311');
INSERT INTO public.t_ds_audit_log VALUES (232, 1, 152648830397760, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-22 11:17:49.425');
INSERT INTO public.t_ds_audit_log VALUES (233, 1, 152648830397760, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 11:17:49.448');
INSERT INTO public.t_ds_audit_log VALUES (234, 1, 152648830397760, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 6, '12', '2025-09-22 11:17:49.458');
INSERT INTO public.t_ds_audit_log VALUES (235, 1, 152648830397760, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 11:17:51.359');
INSERT INTO public.t_ds_audit_log VALUES (236, 1, 152648927087936, '转化_水位行转列', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 11:19:31.094');
INSERT INTO public.t_ds_audit_log VALUES (237, 1, 152648927087936, '转化_水位行转列', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 3, NULL, '2025-09-22 11:19:34.607');
INSERT INTO public.t_ds_audit_log VALUES (238, 1, 152648927087936, '转化_水位行转列', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 11:19:36.383');
INSERT INTO public.t_ds_audit_log VALUES (230, 1, 152648200553792, '水位重复记录稽查JBmDEZ61758510409845', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 33, '11', '2025-09-22 11:06:49.988');
INSERT INTO public.t_ds_audit_log VALUES (239, 1, 152648830397760, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 11:20:23.827');
INSERT INTO public.t_ds_audit_log VALUES (240, 1, 152648927087936, '转化_水位行转列', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 11:21:19.264');
INSERT INTO public.t_ds_audit_log VALUES (241, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-22 11:37:12.762');
INSERT INTO public.t_ds_audit_log VALUES (242, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 11:37:18.148');
INSERT INTO public.t_ds_audit_log VALUES (243, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 11:37:21.049');
INSERT INTO public.t_ds_audit_log VALUES (244, 1, 39, '转化_水位行转列-1-20250922111936725', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 24, NULL, '2025-09-22 11:37:37.721');
INSERT INTO public.t_ds_audit_log VALUES (245, 1, 36, '转化_水位列转行-3-20250922092729816', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 24, NULL, '2025-09-22 11:37:37.721');
INSERT INTO public.t_ds_audit_log VALUES (246, 1, 152414754586944, '站点编码非空与格式稽查t0GWyOL1758273033134', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-22 11:40:59.142');
INSERT INTO public.t_ds_audit_log VALUES (247, 1, 152414792195392, '水位数值范围稽查Vgs9m9g1758273072566', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 11:41:21.144');
INSERT INTO public.t_ds_audit_log VALUES (248, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 43, NULL, '2025-09-22 11:42:59.284');
INSERT INTO public.t_ds_audit_log VALUES (249, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 11:42:59.336');
INSERT INTO public.t_ds_audit_log VALUES (250, 1, 152414792195392, '水位数值范围稽查aD3EDzj1758512581368', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-22 11:43:01.375');
INSERT INTO public.t_ds_audit_log VALUES (251, 1, 152414792195392, '水位数值范围稽查aD3EDzj1758512581368', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 11:43:01.394');
INSERT INTO public.t_ds_audit_log VALUES (252, 1, 152650261388608, '数据完整性稽查GZHrUJx1758512582924', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 11:43:02.93');
INSERT INTO public.t_ds_audit_log VALUES (253, 1, 152650261388608, '数据完整性稽查GZHrUJx1758512582924', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 11:43:02.942');
INSERT INTO public.t_ds_audit_log VALUES (254, 1, 152650261388608, '数据完整性稽查GZHrUJx1758512582924', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 5, '13', '2025-09-22 11:43:02.949');
INSERT INTO public.t_ds_audit_log VALUES (255, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 11:43:04.538');
INSERT INTO public.t_ds_audit_log VALUES (256, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 11:43:04.559');
INSERT INTO public.t_ds_audit_log VALUES (257, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 27, 'latest', '2025-09-22 12:03:11.618');
INSERT INTO public.t_ds_audit_log VALUES (258, 1, 152414792195392, '水位数值范围稽查aD3EDzj1758512581368', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 12:03:15.622');
INSERT INTO public.t_ds_audit_log VALUES (259, 1, 152650261388608, '数据完整性稽查GZHrUJx1758512582924', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 12:03:18.3');
INSERT INTO public.t_ds_audit_log VALUES (260, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 12:03:20.734');
INSERT INTO public.t_ds_audit_log VALUES (261, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 35, NULL, '2025-09-22 12:46:47.625');
INSERT INTO public.t_ds_audit_log VALUES (262, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 12:46:51.495');
INSERT INTO public.t_ds_audit_log VALUES (263, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 12:46:52.631');
INSERT INTO public.t_ds_audit_log VALUES (264, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-22 12:46:53.863');
INSERT INTO public.t_ds_audit_log VALUES (265, 1, 40, '转化_站点表字符串拼接-1-20250922113721586', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 34, NULL, '2025-09-22 12:47:01.114');
INSERT INTO public.t_ds_audit_log VALUES (266, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-22 12:48:34.133');
INSERT INTO public.t_ds_audit_log VALUES (267, 1, 152654997830976, '多表_更新写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 56, NULL, '2025-09-22 13:05:39.168');
INSERT INTO public.t_ds_audit_log VALUES (268, 1, 152654997830976, '多表_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 13:05:42.394');
INSERT INTO public.t_ds_audit_log VALUES (269, 1, 152654997830976, '多表_更新写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 26, 'latest', '2025-09-22 13:05:49.687');
INSERT INTO public.t_ds_audit_log VALUES (270, 1, 152648927087936, '转化_水位行转列', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 55, NULL, '2025-09-22 13:06:01.954');
INSERT INTO public.t_ds_audit_log VALUES (271, 1, 45, '转化_站点数据去重-1-20250922124653986', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-22 13:06:07.322');
INSERT INTO public.t_ds_audit_log VALUES (272, 1, 152654997830976, '多表_更新写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 13:07:36.844');
INSERT INTO public.t_ds_audit_log VALUES (273, 1, 152654997830976, '多表_更新写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 50, NULL, '2025-09-22 13:10:04.125');
INSERT INTO public.t_ds_audit_log VALUES (274, 1, 152655297809728, '多表_全量写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-22 13:10:50.228');
INSERT INTO public.t_ds_audit_log VALUES (275, 1, 152655297809728, '多表_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 13:10:52.311');
INSERT INTO public.t_ds_audit_log VALUES (276, 1, 152655297809728, '多表_全量写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-22 13:10:53.835');
INSERT INTO public.t_ds_audit_log VALUES (277, 1, 46, '多表_更新写-1-20250922130549865', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 13:11:00.768');
INSERT INTO public.t_ds_audit_log VALUES (278, 1, 152655297809728, '多表_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 13:12:28.482');
INSERT INTO public.t_ds_audit_log VALUES (279, 1, 152655501885760, '多表_追加写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 35, NULL, '2025-09-22 13:14:24.021');
INSERT INTO public.t_ds_audit_log VALUES (280, 1, 152655501885760, '多表_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 13:14:26.417');
INSERT INTO public.t_ds_audit_log VALUES (281, 1, 152655501885760, '多表_追加写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 13:14:28.936');
INSERT INTO public.t_ds_audit_log VALUES (282, 1, 47, '多表_全量写-1-20250922131054510', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 13:14:39.564');
INSERT INTO public.t_ds_audit_log VALUES (283, 1, 152655501885760, '多表_追加写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 13:15:34.981');
INSERT INTO public.t_ds_audit_log VALUES (284, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-22 13:23:43.68');
INSERT INTO public.t_ds_audit_log VALUES (285, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 13:23:49.695');
INSERT INTO public.t_ds_audit_log VALUES (286, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 15, 'latest', '2025-09-22 13:23:51.603');
INSERT INTO public.t_ds_audit_log VALUES (287, 1, 48, '多表_追加写-1-20250922131429409', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 13:24:10.84');
INSERT INTO public.t_ds_audit_log VALUES (288, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 13:25:51.934');
INSERT INTO public.t_ds_audit_log VALUES (289, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 31, NULL, '2025-09-22 13:27:45.329');
INSERT INTO public.t_ds_audit_log VALUES (290, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 14, NULL, '2025-09-22 13:27:47.761');
INSERT INTO public.t_ds_audit_log VALUES (291, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 15, 'latest', '2025-09-22 13:27:48.943');
INSERT INTO public.t_ds_audit_log VALUES (292, 1, 49, '单表_全量读_全量写-1-20250922132352279', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 21, NULL, '2025-09-22 13:28:40.393');
INSERT INTO public.t_ds_audit_log VALUES (293, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 13:29:51.746');
INSERT INTO public.t_ds_audit_log VALUES (294, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 31, NULL, '2025-09-22 13:34:27.698');
INSERT INTO public.t_ds_audit_log VALUES (295, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 13:34:30.735');
INSERT INTO public.t_ds_audit_log VALUES (296, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 13:34:32.014');
INSERT INTO public.t_ds_audit_log VALUES (297, 1, 50, '单表_时间增量读_更新写-1-20250922132749601', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 12, NULL, '2025-09-22 13:34:46.721');
INSERT INTO public.t_ds_audit_log VALUES (298, 1, 51, '单表_ID增量读_追加写-1-20250922133432551', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 25, NULL, '2025-09-22 13:37:04.348');
INSERT INTO public.t_ds_audit_log VALUES (299, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 33, NULL, '2025-09-22 13:44:11.845');
INSERT INTO public.t_ds_audit_log VALUES (300, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 46, NULL, '2025-09-22 13:44:26.165');
INSERT INTO public.t_ds_audit_log VALUES (301, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 13:44:30.361');
INSERT INTO public.t_ds_audit_log VALUES (302, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 13:44:31.966');
INSERT INTO public.t_ds_audit_log VALUES (303, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 17, 'latest', '2025-09-22 13:44:36.128');
INSERT INTO public.t_ds_audit_log VALUES (304, 1, 152414792195392, '水位数值范围稽查aD3EDzj1758512581368', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 38, NULL, '2025-09-22 13:49:33.21');
INSERT INTO public.t_ds_audit_log VALUES (305, 1, 152648200553792, '水位重复记录稽查JBmDEZ61758510409845', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-22 13:50:20.239');
INSERT INTO public.t_ds_audit_log VALUES (306, 1, 152650261388608, '数据完整性稽查GZHrUJx1758512582924', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-22 13:50:55.371');
INSERT INTO public.t_ds_audit_log VALUES (307, 1, 152650261388608, '数据完整性稽查huxkK4P1758520316836', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 68, NULL, '2025-09-22 13:51:56.856');
INSERT INTO public.t_ds_audit_log VALUES (308, 1, 152650261388608, '数据完整性稽查huxkK4P1758520316836', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 13:51:56.935');
INSERT INTO public.t_ds_audit_log VALUES (309, 1, 152650261388608, '数据完整性稽查huxkK4P1758520316836', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-22 13:51:59.271');
INSERT INTO public.t_ds_audit_log VALUES (310, 1, 152650261388608, '数据完整性稽查huxkK4P1758520316836', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 13:52:28.183');
INSERT INTO public.t_ds_audit_log VALUES (311, 1, 152650261388608, '数据完整性稽查HzqLIag1758520382690', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 39, NULL, '2025-09-22 13:53:02.701');
INSERT INTO public.t_ds_audit_log VALUES (312, 1, 152650261388608, '数据完整性稽查HzqLIag1758520382690', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 13:53:02.76');
INSERT INTO public.t_ds_audit_log VALUES (313, 1, 152650261388608, '数据完整性稽查HzqLIag1758520382690', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 39, 'latest', '2025-09-22 13:53:06.107');
INSERT INTO public.t_ds_audit_log VALUES (314, 1, 152650261388608, '数据完整性稽查HzqLIag1758520382690', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-22 13:53:28.617');
INSERT INTO public.t_ds_audit_log VALUES (315, 1, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 13:57:17.757');
INSERT INTO public.t_ds_audit_log VALUES (316, 1, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 13:57:17.796');
INSERT INTO public.t_ds_audit_log VALUES (317, 1, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 13, '14', '2025-09-22 13:57:17.813');
INSERT INTO public.t_ds_audit_log VALUES (318, 1, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-22 13:57:18.688');
INSERT INTO public.t_ds_audit_log VALUES (319, 1, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-22 13:59:56.93');
INSERT INTO public.t_ds_audit_log VALUES (320, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 51, NULL, '2025-09-22 14:02:24.003');
INSERT INTO public.t_ds_audit_log VALUES (321, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 14:02:36.719');
INSERT INTO public.t_ds_audit_log VALUES (322, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 14:02:39.563');
INSERT INTO public.t_ds_audit_log VALUES (323, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 14:02:43.664');
INSERT INTO public.t_ds_audit_log VALUES (324, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 14:02:44.866');
INSERT INTO public.t_ds_audit_log VALUES (325, 1, 52, '清洗_水位格式标准化-2-20250922134437009', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 16, NULL, '2025-09-22 14:02:47.997');
INSERT INTO public.t_ds_audit_log VALUES (326, 1, 56, '清洗_水位缺失补全-2-20250922140245837', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 72, NULL, '2025-09-22 14:06:01.087');
INSERT INTO public.t_ds_audit_log VALUES (327, 1, 56, '清洗_水位缺失补全-2-20250922140245837', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 17, NULL, '2025-09-22 14:06:03.607');
INSERT INTO public.t_ds_audit_log VALUES (328, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 43, NULL, '2025-09-22 14:13:36.278');
INSERT INTO public.t_ds_audit_log VALUES (329, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 14:13:38.487');
INSERT INTO public.t_ds_audit_log VALUES (330, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 17, NULL, '2025-09-22 14:13:40.09');
INSERT INTO public.t_ds_audit_log VALUES (331, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 15, 'latest', '2025-09-22 14:13:41.147');
INSERT INTO public.t_ds_audit_log VALUES (332, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 21, 'latest', '2025-09-22 14:17:50.119');
INSERT INTO public.t_ds_audit_log VALUES (333, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 34, 'latest', '2025-09-22 14:19:55.761');
INSERT INTO public.t_ds_audit_log VALUES (334, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 14:20:53.913');
INSERT INTO public.t_ds_audit_log VALUES (335, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-22 14:20:55.595');
INSERT INTO public.t_ds_audit_log VALUES (336, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 20, 'latest', '2025-09-22 14:20:57.611');
INSERT INTO public.t_ds_audit_log VALUES (337, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 14:22:14.914');
INSERT INTO public.t_ds_audit_log VALUES (338, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 376, NULL, '2025-09-22 14:35:43.622');
INSERT INTO public.t_ds_audit_log VALUES (339, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 14:35:46.11');
INSERT INTO public.t_ds_audit_log VALUES (340, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 20, 'latest', '2025-09-22 14:35:47.345');
INSERT INTO public.t_ds_audit_log VALUES (341, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 14:41:19.983');
INSERT INTO public.t_ds_audit_log VALUES (342, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 50, NULL, '2025-09-22 14:41:33.246');
INSERT INTO public.t_ds_audit_log VALUES (343, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 18, NULL, '2025-09-22 14:41:57.241');
INSERT INTO public.t_ds_audit_log VALUES (344, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 14:41:58.986');
INSERT INTO public.t_ds_audit_log VALUES (345, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 14:43:51.927');
INSERT INTO public.t_ds_audit_log VALUES (346, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 14:43:53.362');
INSERT INTO public.t_ds_audit_log VALUES (347, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 14:43:59.329');
INSERT INTO public.t_ds_audit_log VALUES (348, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-22 14:45:36.64');
INSERT INTO public.t_ds_audit_log VALUES (349, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 14:47:57.692');
INSERT INTO public.t_ds_audit_log VALUES (350, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-22 14:59:15.675');
INSERT INTO public.t_ds_audit_log VALUES (351, 1, 64, 'Flink流_水位信息采集-2-20250922144758369', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (352, 1, 63, 'Flink流_水位信息采集-2-20250922144537365', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (353, 1, 62, 'Flink流_水位信息采集-2-20250922144400085', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (354, 1, 61, 'Flink流_水位信息采集-1-20250922143548086', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (355, 1, 60, '清洗_水位缺失补全-2-20250922142058041', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (356, 1, 59, '清洗_水位异常值处理-1-20250922141956077', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (357, 1, 58, '清洗_水位异常值处理-1-20250922141750180', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (358, 1, 57, '清洗_水位异常值处理-1-20250922141341366', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 67, NULL, '2025-09-22 14:59:23.664');
INSERT INTO public.t_ds_audit_log VALUES (359, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 15:05:24.379');
INSERT INTO public.t_ds_audit_log VALUES (360, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 44, NULL, '2025-09-22 15:07:02.299');
INSERT INTO public.t_ds_audit_log VALUES (361, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 15:07:04.064');
INSERT INTO public.t_ds_audit_log VALUES (362, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-22 15:07:05.164');
INSERT INTO public.t_ds_audit_log VALUES (363, 1, 65, 'Flink流_水位信息采集-2-20250922145916390', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:07:11.278');
INSERT INTO public.t_ds_audit_log VALUES (364, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 15:08:02.698');
INSERT INTO public.t_ds_audit_log VALUES (365, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 15:16:41.614');
INSERT INTO public.t_ds_audit_log VALUES (366, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 34, NULL, '2025-09-22 15:21:21.33');
INSERT INTO public.t_ds_audit_log VALUES (367, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 15:21:22.943');
INSERT INTO public.t_ds_audit_log VALUES (368, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-22 15:21:23.803');
INSERT INTO public.t_ds_audit_log VALUES (369, 1, 66, 'Flink流_水位信息采集-3-20250922150705866', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 11, NULL, '2025-09-22 15:21:29.329');
INSERT INTO public.t_ds_audit_log VALUES (370, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:21:51.326');
INSERT INTO public.t_ds_audit_log VALUES (371, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 37, NULL, '2025-09-22 15:22:02.404');
INSERT INTO public.t_ds_audit_log VALUES (372, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 15:22:03.833');
INSERT INTO public.t_ds_audit_log VALUES (373, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 15:22:05.175');
INSERT INTO public.t_ds_audit_log VALUES (374, 1, 67, 'Flink流_水位信息采集-5-20250922152124631', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:22:11.106');
INSERT INTO public.t_ds_audit_log VALUES (375, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:22:31.784');
INSERT INTO public.t_ds_audit_log VALUES (376, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-22 15:22:54.419');
INSERT INTO public.t_ds_audit_log VALUES (377, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 15:22:58.74');
INSERT INTO public.t_ds_audit_log VALUES (378, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 15:22:59.844');
INSERT INTO public.t_ds_audit_log VALUES (379, 1, 68, 'Flink流_水位信息采集-6-20250922152205226', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:23:05.292');
INSERT INTO public.t_ds_audit_log VALUES (380, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 15:23:26.399');
INSERT INTO public.t_ds_audit_log VALUES (381, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-22 15:24:10.626');
INSERT INTO public.t_ds_audit_log VALUES (382, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:24:10.656');
INSERT INTO public.t_ds_audit_log VALUES (383, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 7, 'latest', '2025-09-22 15:24:13.179');
INSERT INTO public.t_ds_audit_log VALUES (384, 1, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 15:24:16.589');
INSERT INTO public.t_ds_audit_log VALUES (385, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 25, NULL, '2025-09-22 15:24:51.822');
INSERT INTO public.t_ds_audit_log VALUES (386, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 15:24:53.567');
INSERT INTO public.t_ds_audit_log VALUES (387, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-22 15:24:54.478');
INSERT INTO public.t_ds_audit_log VALUES (388, 1, 69, 'Flink流_水位信息采集-7-20250922152300401', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 8, NULL, '2025-09-22 15:25:01.882');
INSERT INTO public.t_ds_audit_log VALUES (389, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 15:25:12.519');
INSERT INTO public.t_ds_audit_log VALUES (390, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 15:26:54.053');
INSERT INTO public.t_ds_audit_log VALUES (391, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 45, NULL, '2025-09-22 15:27:11.854');
INSERT INTO public.t_ds_audit_log VALUES (392, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 15:27:13.435');
INSERT INTO public.t_ds_audit_log VALUES (393, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 15:27:14.816');
INSERT INTO public.t_ds_audit_log VALUES (394, 1, 71, 'Flink流_水位信息采集-8-20250922152454678', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:27:19.298');
INSERT INTO public.t_ds_audit_log VALUES (395, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 15:27:34.971');
INSERT INTO public.t_ds_audit_log VALUES (396, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 15:27:41.333');
INSERT INTO public.t_ds_audit_log VALUES (397, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-22 15:28:22.867');
INSERT INTO public.t_ds_audit_log VALUES (398, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 36, NULL, '2025-09-22 15:34:08.63');
INSERT INTO public.t_ds_audit_log VALUES (399, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 15:34:10.392');
INSERT INTO public.t_ds_audit_log VALUES (400, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-22 15:34:11.443');
INSERT INTO public.t_ds_audit_log VALUES (403, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 15:37:26.886');
INSERT INTO public.t_ds_audit_log VALUES (407, 1, 75, 'Flink流_水位信息采集-13-20250922153728406', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 21, NULL, '2025-09-22 15:41:04.08');
INSERT INTO public.t_ds_audit_log VALUES (408, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:41:07.727');
INSERT INTO public.t_ds_audit_log VALUES (410, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 15:41:15.74');
INSERT INTO public.t_ds_audit_log VALUES (401, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 15:37:17.907');
INSERT INTO public.t_ds_audit_log VALUES (405, 1, 74, 'Flink流_水位信息采集-12-20250922153411808', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 17, NULL, '2025-09-22 15:37:32.208');
INSERT INTO public.t_ds_audit_log VALUES (406, 1, 73, 'Flink流_水位信息采集-9-20250922152714927', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 17, NULL, '2025-09-22 15:37:32.208');
INSERT INTO public.t_ds_audit_log VALUES (402, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-22 15:37:25.782');
INSERT INTO public.t_ds_audit_log VALUES (404, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 15:37:27.906');
INSERT INTO public.t_ds_audit_log VALUES (409, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 15:41:13.816');
INSERT INTO public.t_ds_audit_log VALUES (411, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 15, 'latest', '2025-09-22 15:41:16.788');
INSERT INTO public.t_ds_audit_log VALUES (412, 1, 75, 'Flink流_水位信息采集-13-20250922153728406', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:41:34.418');
INSERT INTO public.t_ds_audit_log VALUES (413, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 15:42:03.924');
INSERT INTO public.t_ds_audit_log VALUES (414, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-22 15:42:18.856');
INSERT INTO public.t_ds_audit_log VALUES (415, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 15:42:20.591');
INSERT INTO public.t_ds_audit_log VALUES (416, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 15:42:21.628');
INSERT INTO public.t_ds_audit_log VALUES (417, 1, 76, 'Flink流_水位信息采集-14-20250922154117040', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 15:42:24.727');
INSERT INTO public.t_ds_audit_log VALUES (418, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-22 15:43:45.452');
INSERT INTO public.t_ds_audit_log VALUES (419, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 15:43:47.344');
INSERT INTO public.t_ds_audit_log VALUES (420, 1, 77, 'Flink流_水位信息采集-15-20250922154221721', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 13, NULL, '2025-09-22 15:43:51.39');
INSERT INTO public.t_ds_audit_log VALUES (421, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 15:43:54.49');
INSERT INTO public.t_ds_audit_log VALUES (422, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:46:15.128');
INSERT INTO public.t_ds_audit_log VALUES (423, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 15:46:31.793');
INSERT INTO public.t_ds_audit_log VALUES (424, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 15:47:22.673');
INSERT INTO public.t_ds_audit_log VALUES (425, 1, 78, 'Flink批_水位信息采集-2-20250922154354804', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 19, NULL, '2025-09-22 15:57:24.735');
INSERT INTO public.t_ds_audit_log VALUES (426, 1, 77, 'Flink流_水位信息采集-15-20250922154221721', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 19, NULL, '2025-09-22 15:57:24.735');
INSERT INTO public.t_ds_audit_log VALUES (427, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 36, NULL, '2025-09-22 15:58:03.229');
INSERT INTO public.t_ds_audit_log VALUES (428, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 19, NULL, '2025-09-22 15:58:28.483');
INSERT INTO public.t_ds_audit_log VALUES (429, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 15:58:30.093');
INSERT INTO public.t_ds_audit_log VALUES (430, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 15:58:31.274');
INSERT INTO public.t_ds_audit_log VALUES (431, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:01:36.173');
INSERT INTO public.t_ds_audit_log VALUES (432, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 24, NULL, '2025-09-22 16:01:45.983');
INSERT INTO public.t_ds_audit_log VALUES (433, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 16:01:47.794');
INSERT INTO public.t_ds_audit_log VALUES (434, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 16:01:48.792');
INSERT INTO public.t_ds_audit_log VALUES (435, 1, 79, 'Flink流_水位信息采集-17-20250922155831621', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-22 16:01:54.211');
INSERT INTO public.t_ds_audit_log VALUES (436, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:06:13.382');
INSERT INTO public.t_ds_audit_log VALUES (437, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 16:06:23.263');
INSERT INTO public.t_ds_audit_log VALUES (438, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 16:06:24.811');
INSERT INTO public.t_ds_audit_log VALUES (439, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 16:06:25.944');
INSERT INTO public.t_ds_audit_log VALUES (440, 1, 80, 'Flink流_水位信息采集-18-20250922160149724', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 16:06:30.204');
INSERT INTO public.t_ds_audit_log VALUES (441, 1, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 18, NULL, '2025-09-22 16:07:25.874');
INSERT INTO public.t_ds_audit_log VALUES (442, 1, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:07:25.898');
INSERT INTO public.t_ds_audit_log VALUES (443, 1, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 11, '15', '2025-09-22 16:07:25.91');
INSERT INTO public.t_ds_audit_log VALUES (444, 1, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 16:07:28.084');
INSERT INTO public.t_ds_audit_log VALUES (445, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:08:15.248');
INSERT INTO public.t_ds_audit_log VALUES (446, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-22 16:08:21.504');
INSERT INTO public.t_ds_audit_log VALUES (447, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 16:08:23.16');
INSERT INTO public.t_ds_audit_log VALUES (448, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 16:08:24.058');
INSERT INTO public.t_ds_audit_log VALUES (449, 1, 81, 'Flink流_水位信息采集-19-20250922160626473', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 7, NULL, '2025-09-22 16:08:29.561');
INSERT INTO public.t_ds_audit_log VALUES (450, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:10:37.256');
INSERT INTO public.t_ds_audit_log VALUES (451, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 16:10:42.568');
INSERT INTO public.t_ds_audit_log VALUES (452, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 16:10:43.939');
INSERT INTO public.t_ds_audit_log VALUES (453, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 16:10:44.975');
INSERT INTO public.t_ds_audit_log VALUES (454, 1, 83, 'Flink流_水位信息采集-20-20250922160824199', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 11, NULL, '2025-09-22 16:10:50.123');
INSERT INTO public.t_ds_audit_log VALUES (455, 1, 84, 'Flink流_水位信息采集-21-20250922161045636', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 14, NULL, '2025-09-22 16:11:19.308');
INSERT INTO public.t_ds_audit_log VALUES (456, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:11:24.465');
INSERT INTO public.t_ds_audit_log VALUES (457, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-22 16:14:25.291');
INSERT INTO public.t_ds_audit_log VALUES (458, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:14:26.505');
INSERT INTO public.t_ds_audit_log VALUES (459, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 16:14:27.697');
INSERT INTO public.t_ds_audit_log VALUES (460, 1, 84, 'Flink流_水位信息采集-21-20250922161045636', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 20, NULL, '2025-09-22 16:14:32.273');
INSERT INTO public.t_ds_audit_log VALUES (461, 1, 85, 'Flink流_水位信息采集-22-20250922161428303', 'Process', 'Rerun', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 254, NULL, '2025-09-22 16:14:52.303');
INSERT INTO public.t_ds_audit_log VALUES (462, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 3, NULL, '2025-09-22 16:15:41.187');
INSERT INTO public.t_ds_audit_log VALUES (463, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 16:19:00.251');
INSERT INTO public.t_ds_audit_log VALUES (464, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:19:02.371');
INSERT INTO public.t_ds_audit_log VALUES (465, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 16:19:03.443');
INSERT INTO public.t_ds_audit_log VALUES (466, 1, 85, 'Flink流_水位信息采集-22-20250922161428303', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-22 16:19:14.837');
INSERT INTO public.t_ds_audit_log VALUES (467, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 16:19:32.351');
INSERT INTO public.t_ds_audit_log VALUES (468, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 38, NULL, '2025-09-22 16:29:10.632');
INSERT INTO public.t_ds_audit_log VALUES (469, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 16:29:12.73');
INSERT INTO public.t_ds_audit_log VALUES (470, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-22 16:29:13.67');
INSERT INTO public.t_ds_audit_log VALUES (471, 1, 86, 'Flink流_水位信息采集-23-20250922161904415', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-22 16:29:20.081');
INSERT INTO public.t_ds_audit_log VALUES (472, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 16:31:25.841');
INSERT INTO public.t_ds_audit_log VALUES (473, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 48, NULL, '2025-09-22 16:32:10.823');
INSERT INTO public.t_ds_audit_log VALUES (474, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 16:32:12.636');
INSERT INTO public.t_ds_audit_log VALUES (475, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-22 16:32:13.862');
INSERT INTO public.t_ds_audit_log VALUES (476, 1, 87, 'Flink流_水位信息采集-24-20250922162914120', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 11, NULL, '2025-09-22 16:32:18.438');
INSERT INTO public.t_ds_audit_log VALUES (477, 1, 88, 'Flink流_水位信息采集-25-20250922163213945', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 16:32:39.833');
INSERT INTO public.t_ds_audit_log VALUES (478, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 16:32:48.241');
INSERT INTO public.t_ds_audit_log VALUES (479, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 16:32:54.903');
INSERT INTO public.t_ds_audit_log VALUES (480, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 16:32:56.825');
INSERT INTO public.t_ds_audit_log VALUES (481, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-22 16:32:57.986');
INSERT INTO public.t_ds_audit_log VALUES (482, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:33:25.091');
INSERT INTO public.t_ds_audit_log VALUES (483, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 48, NULL, '2025-09-22 16:33:58.322');
INSERT INTO public.t_ds_audit_log VALUES (484, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:34:00.121');
INSERT INTO public.t_ds_audit_log VALUES (485, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-22 16:34:01.195');
INSERT INTO public.t_ds_audit_log VALUES (486, 1, 89, 'Flink批_水位信息采集-4-20250922163258934', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 16:34:04.949');
INSERT INTO public.t_ds_audit_log VALUES (487, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 16:34:34.87');
INSERT INTO public.t_ds_audit_log VALUES (488, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 64, NULL, '2025-09-22 16:37:15.03');
INSERT INTO public.t_ds_audit_log VALUES (489, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 16:37:16.862');
INSERT INTO public.t_ds_audit_log VALUES (490, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 19, 'latest', '2025-09-22 16:37:18.132');
INSERT INTO public.t_ds_audit_log VALUES (491, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-22 16:42:55.387');
INSERT INTO public.t_ds_audit_log VALUES (492, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 64, NULL, '2025-09-22 16:44:33.556');
INSERT INTO public.t_ds_audit_log VALUES (493, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 16:44:40.119');
INSERT INTO public.t_ds_audit_log VALUES (494, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 31, 'latest', '2025-09-22 16:44:41.57');
INSERT INTO public.t_ds_audit_log VALUES (495, 1, 91, 'Flink批_水位信息采集-6-20250922163718874', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 33, NULL, '2025-09-22 16:44:48.188');
INSERT INTO public.t_ds_audit_log VALUES (496, 1, 90, 'Flink批_水位信息采集-5-20250922163401524', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 33, NULL, '2025-09-22 16:44:48.188');
INSERT INTO public.t_ds_audit_log VALUES (497, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 16:45:19.55');
INSERT INTO public.t_ds_audit_log VALUES (498, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 45, NULL, '2025-09-22 16:53:53.436');
INSERT INTO public.t_ds_audit_log VALUES (499, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-22 16:53:58.449');
INSERT INTO public.t_ds_audit_log VALUES (500, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 16:53:59.55');
INSERT INTO public.t_ds_audit_log VALUES (501, 1, 92, 'Flink批_水位信息采集-7-20250922164442599', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 20, NULL, '2025-09-22 16:54:03.789');
INSERT INTO public.t_ds_audit_log VALUES (502, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 17:02:11.148');
INSERT INTO public.t_ds_audit_log VALUES (503, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 17:02:19.254');
INSERT INTO public.t_ds_audit_log VALUES (504, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 17:02:21.424');
INSERT INTO public.t_ds_audit_log VALUES (505, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 18, 'latest', '2025-09-22 17:02:23.071');
INSERT INTO public.t_ds_audit_log VALUES (506, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-22 17:03:36.421');
INSERT INTO public.t_ds_audit_log VALUES (507, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 33, NULL, '2025-09-22 17:03:40.393');
INSERT INTO public.t_ds_audit_log VALUES (508, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 17:03:42.881');
INSERT INTO public.t_ds_audit_log VALUES (509, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 17:03:43.936');
INSERT INTO public.t_ds_audit_log VALUES (510, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 17:03:52.034');
INSERT INTO public.t_ds_audit_log VALUES (511, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 17:04:28.141');
INSERT INTO public.t_ds_audit_log VALUES (512, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 17:04:40.305');
INSERT INTO public.t_ds_audit_log VALUES (513, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 33, NULL, '2025-09-22 17:18:23.94');
INSERT INTO public.t_ds_audit_log VALUES (514, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 17:18:25.775');
INSERT INTO public.t_ds_audit_log VALUES (515, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 17, 'latest', '2025-09-22 17:18:27.523');
INSERT INTO public.t_ds_audit_log VALUES (516, 1, 95, 'SparkSQL_水位日统计报表-2-20250922170344532', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 26, NULL, '2025-09-22 17:18:32.937');
INSERT INTO public.t_ds_audit_log VALUES (517, 1, 94, 'SparkSQL_水位日统计报表-1-20250922170223394', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 26, NULL, '2025-09-22 17:18:32.937');
INSERT INTO public.t_ds_audit_log VALUES (518, 1, 93, 'Flink批_水位信息采集-8-20250922165400559', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 26, NULL, '2025-09-22 17:18:32.937');
INSERT INTO public.t_ds_audit_log VALUES (519, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 17:19:48.553');
INSERT INTO public.t_ds_audit_log VALUES (520, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 60, NULL, '2025-09-22 17:25:00.556');
INSERT INTO public.t_ds_audit_log VALUES (521, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 17:25:02.205');
INSERT INTO public.t_ds_audit_log VALUES (522, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 17:25:28.9');
INSERT INTO public.t_ds_audit_log VALUES (523, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-22 17:25:52.409');
INSERT INTO public.t_ds_audit_log VALUES (524, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 14, NULL, '2025-09-22 17:25:54.927');
INSERT INTO public.t_ds_audit_log VALUES (525, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-22 17:25:55.941');
INSERT INTO public.t_ds_audit_log VALUES (526, 1, 96, 'SparkSQL_水位日统计报表-3-20250922171828442', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-22 17:26:03.138');
INSERT INTO public.t_ds_audit_log VALUES (527, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 17:26:43.192');
INSERT INTO public.t_ds_audit_log VALUES (528, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 17:27:06.003');
INSERT INTO public.t_ds_audit_log VALUES (529, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 17:27:07.997');
INSERT INTO public.t_ds_audit_log VALUES (530, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-22 17:27:08.982');
INSERT INTO public.t_ds_audit_log VALUES (531, 1, 97, 'Flink流_水位信息采集-27-20250922172556215', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 13, NULL, '2025-09-22 17:27:18.168');
INSERT INTO public.t_ds_audit_log VALUES (532, 1, 98, 'Flink流_水位信息采集-28-20250922172709349', 'Process', 'Stop', 'EXECUTE_ACTION_TO_PROCESS_INSTANCE_NOTES', 45, NULL, '2025-09-22 17:29:10.344');
INSERT INTO public.t_ds_audit_log VALUES (533, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 17:29:13.859');
INSERT INTO public.t_ds_audit_log VALUES (534, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 47, NULL, '2025-09-22 17:32:20.312');
INSERT INTO public.t_ds_audit_log VALUES (535, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 17:32:22.131');
INSERT INTO public.t_ds_audit_log VALUES (536, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-22 17:32:23.604');
INSERT INTO public.t_ds_audit_log VALUES (537, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 17:34:08.014');
INSERT INTO public.t_ds_audit_log VALUES (538, 1, 152670679994688, '1111', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-22 17:35:04.905');
INSERT INTO public.t_ds_audit_log VALUES (539, 1, 152670679994688, '1111', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 17:35:08.938');
INSERT INTO public.t_ds_audit_log VALUES (540, 1, 152670679994688, '1111', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, '1', '2025-09-22 17:35:11.423');
INSERT INTO public.t_ds_audit_log VALUES (541, 1, 152670679994688, '1111', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 17:37:20.454');
INSERT INTO public.t_ds_audit_log VALUES (542, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 35, NULL, '2025-09-22 17:39:23.501');
INSERT INTO public.t_ds_audit_log VALUES (543, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 49, NULL, '2025-09-22 17:43:36.643');
INSERT INTO public.t_ds_audit_log VALUES (544, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 21, NULL, '2025-09-22 17:43:43.36');
INSERT INTO public.t_ds_audit_log VALUES (545, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 19, '31', '2025-09-22 17:43:46.782');
INSERT INTO public.t_ds_audit_log VALUES (546, 1, 100, '1111-1-20250922173512393', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 37, NULL, '2025-09-22 17:43:54.29');
INSERT INTO public.t_ds_audit_log VALUES (547, 1, 99, 'Flink流_水位信息采集-29-20250922173223954', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 37, NULL, '2025-09-22 17:43:54.29');
INSERT INTO public.t_ds_audit_log VALUES (548, 1, 152669873712448, 'Hive_水位日统计报表', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 118, NULL, '2025-09-22 17:45:46.446');
INSERT INTO public.t_ds_audit_log VALUES (549, 1, 152670679994688, '1111', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 220, NULL, '2025-09-22 17:46:07.561');
INSERT INTO public.t_ds_audit_log VALUES (550, 1, 152667889198400, 'SparkSQL_水位日统计报表', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 28, NULL, '2025-09-22 17:46:09.224');
INSERT INTO public.t_ds_audit_log VALUES (551, 1, 152671720176960, 'SparkSQL_水位日统计报表', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-22 17:52:12.447');
INSERT INTO public.t_ds_audit_log VALUES (552, 1, 152669873712448, 'Hive_水位日统计报表', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 32, NULL, '2025-09-22 18:00:48.74');
INSERT INTO public.t_ds_audit_log VALUES (553, 1, 152672440882496, 'Hive_水位日统计报表', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-22 18:03:55.89');
INSERT INTO public.t_ds_audit_log VALUES (554, 1, 152672440882496, 'Hive_水位日统计报表', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 36, NULL, '2025-09-22 18:06:41.781');
INSERT INTO public.t_ds_audit_log VALUES (555, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 19, NULL, '2025-09-22 18:07:03.264');
INSERT INTO public.t_ds_audit_log VALUES (556, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 63, NULL, '2025-09-22 18:07:40.008');
INSERT INTO public.t_ds_audit_log VALUES (557, 1, 152672766952768, 'MySQL_水位日统计', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-22 18:13:05.608');
INSERT INTO public.t_ds_audit_log VALUES (558, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 18:17:30.6');
INSERT INTO public.t_ds_audit_log VALUES (559, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 18:17:31.871');
INSERT INTO public.t_ds_audit_log VALUES (560, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 18:17:33.057');
INSERT INTO public.t_ds_audit_log VALUES (561, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 18:17:34.551');
INSERT INTO public.t_ds_audit_log VALUES (562, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 18:17:35.905');
INSERT INTO public.t_ds_audit_log VALUES (563, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 18:17:37.093');
INSERT INTO public.t_ds_audit_log VALUES (564, 1, 152655501885760, '多表_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 18:17:38.882');
INSERT INTO public.t_ds_audit_log VALUES (565, 1, 152655297809728, '多表_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-22 18:17:40.015');
INSERT INTO public.t_ds_audit_log VALUES (566, 1, 152654997830976, '多表_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 18:17:41.086');
INSERT INTO public.t_ds_audit_log VALUES (567, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 18:17:43.163');
INSERT INTO public.t_ds_audit_log VALUES (568, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 18:17:45.555');
INSERT INTO public.t_ds_audit_log VALUES (569, 1, 152648927087936, '转化_水位行转列', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 18:17:46.773');
INSERT INTO public.t_ds_audit_log VALUES (570, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-22 18:17:48.037');
INSERT INTO public.t_ds_audit_log VALUES (571, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-22 18:17:49.348');
INSERT INTO public.t_ds_audit_log VALUES (572, 1, 152673403135296, '作业_水位预警计算_串行', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 45, NULL, '2025-09-22 18:19:24.202');
INSERT INTO public.t_ds_audit_log VALUES (573, 1, 152673403135296, '作业_水位预警计算_串行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-22 18:19:42.89');
INSERT INTO public.t_ds_audit_log VALUES (574, 1, 152673582845248, '作业_水位小时均值_并行', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 63, NULL, '2025-09-22 18:22:19.684');
INSERT INTO public.t_ds_audit_log VALUES (575, 1, 152673582845248, '作业_水位小时均值_并行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 43, NULL, '2025-09-22 18:23:31.245');
INSERT INTO public.t_ds_audit_log VALUES (576, 1, 152673582845248, '作业_水位小时均值_并行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 41, NULL, '2025-09-22 18:23:38.429');
INSERT INTO public.t_ds_audit_log VALUES (577, 1, 152673582845248, '作业_水位小时均值_并行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 50, NULL, '2025-09-22 18:23:46.538');
INSERT INTO public.t_ds_audit_log VALUES (578, 1, 152673582845248, '作业_水位小时均值_并行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 32, NULL, '2025-09-22 18:23:52.564');
INSERT INTO public.t_ds_audit_log VALUES (579, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 18:28:04.304');
INSERT INTO public.t_ds_audit_log VALUES (580, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-22 19:43:20.071');
INSERT INTO public.t_ds_audit_log VALUES (581, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 14, NULL, '2025-09-22 19:49:05.569');
INSERT INTO public.t_ds_audit_log VALUES (582, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 3, NULL, '2025-09-22 20:07:35.318');
INSERT INTO public.t_ds_audit_log VALUES (583, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-22 20:07:36.961');
INSERT INTO public.t_ds_audit_log VALUES (584, 1, 152672440882496, 'Hive_水位日统计报表', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-22 20:07:43.013');
INSERT INTO public.t_ds_audit_log VALUES (585, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-22 20:07:44.93');
INSERT INTO public.t_ds_audit_log VALUES (586, 1, 152672766952768, 'MySQL_水位日统计', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-22 20:07:46.498');
INSERT INTO public.t_ds_audit_log VALUES (587, 1, 152671720176960, 'SparkSQL_水位日统计报表', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 242, NULL, '2025-09-23 09:00:24.624');
INSERT INTO public.t_ds_audit_log VALUES (588, 1, 152671720176960, 'SparkSQL_水位日统计报表', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 133, NULL, '2025-09-23 09:03:13.805');
INSERT INTO public.t_ds_audit_log VALUES (589, 1, 152726374059328, 'SparkSQL_水位日统计报表', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 28, NULL, '2025-09-23 09:03:18.46');
INSERT INTO public.t_ds_audit_log VALUES (590, 1, 152726374059328, 'SparkSQL_水位日统计报表', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 50, NULL, '2025-09-23 09:09:26.331');
INSERT INTO public.t_ds_audit_log VALUES (591, 1, 152726374059328, 'SparkSQL_水位日统计报表', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-23 09:10:00.429');
INSERT INTO public.t_ds_audit_log VALUES (592, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 66, NULL, '2025-09-23 10:10:16.609');
INSERT INTO public.t_ds_audit_log VALUES (593, 1, 152673403135296, '作业_水位预警计算_串行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 80, NULL, '2025-09-23 10:10:23.442');
INSERT INTO public.t_ds_audit_log VALUES (594, 1, 152673403135296, '作业_水位预警计算_串行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 163, 'latest', '2025-09-23 10:10:24.873');
INSERT INTO public.t_ds_audit_log VALUES (595, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 174, NULL, '2025-09-23 11:28:43.087');
INSERT INTO public.t_ds_audit_log VALUES (596, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-23 11:28:43.293');
INSERT INTO public.t_ds_audit_log VALUES (597, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-23 11:28:44.788');
INSERT INTO public.t_ds_audit_log VALUES (598, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-23 11:28:44.831');
INSERT INTO public.t_ds_audit_log VALUES (599, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 43, 'latest', '2025-09-23 11:28:48.116');
INSERT INTO public.t_ds_audit_log VALUES (600, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-23 11:28:51.012');
INSERT INTO public.t_ds_audit_log VALUES (601, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-23 11:28:53.03');
INSERT INTO public.t_ds_audit_log VALUES (602, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-23 11:28:54.868');
INSERT INTO public.t_ds_audit_log VALUES (603, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-23 11:28:56.735');
INSERT INTO public.t_ds_audit_log VALUES (604, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-23 11:28:56.766');
INSERT INTO public.t_ds_audit_log VALUES (605, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-23 11:28:57.816');
INSERT INTO public.t_ds_audit_log VALUES (606, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-23 11:29:22.356');
INSERT INTO public.t_ds_audit_log VALUES (607, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 15, 'latest', '2025-09-23 11:30:57.424');
INSERT INTO public.t_ds_audit_log VALUES (608, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-23 11:30:59.915');
INSERT INTO public.t_ds_audit_log VALUES (609, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-23 11:31:01.637');
INSERT INTO public.t_ds_audit_log VALUES (610, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-23 11:31:03.339');
INSERT INTO public.t_ds_audit_log VALUES (611, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-23 11:31:05.022');
INSERT INTO public.t_ds_audit_log VALUES (612, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-23 11:31:06.815');
INSERT INTO public.t_ds_audit_log VALUES (613, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-23 11:31:07.831');
INSERT INTO public.t_ds_audit_log VALUES (614, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 20, 'latest', '2025-09-23 11:33:01.117');
INSERT INTO public.t_ds_audit_log VALUES (615, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-23 11:33:03.511');
INSERT INTO public.t_ds_audit_log VALUES (616, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-23 11:33:05.273');
INSERT INTO public.t_ds_audit_log VALUES (617, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-23 11:33:07.109');
INSERT INTO public.t_ds_audit_log VALUES (618, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-23 11:33:09.082');
INSERT INTO public.t_ds_audit_log VALUES (619, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-23 11:37:06.349');
INSERT INTO public.t_ds_audit_log VALUES (620, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-23 11:37:08.496');
INSERT INTO public.t_ds_audit_log VALUES (621, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-23 11:37:10.365');
INSERT INTO public.t_ds_audit_log VALUES (622, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-23 11:37:12.191');
INSERT INTO public.t_ds_audit_log VALUES (623, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-23 11:37:13.864');
INSERT INTO public.t_ds_audit_log VALUES (624, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 62, NULL, '2025-09-26 13:59:41.912');
INSERT INTO public.t_ds_audit_log VALUES (625, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 20, NULL, '2025-09-26 13:59:43.482');
INSERT INTO public.t_ds_audit_log VALUES (626, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 590, NULL, '2025-09-30 14:32:03.084');
INSERT INTO public.t_ds_audit_log VALUES (627, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 263, NULL, '2025-09-30 14:34:19.825');
INSERT INTO public.t_ds_audit_log VALUES (628, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-30 14:34:22.485');
INSERT INTO public.t_ds_audit_log VALUES (629, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 99, 'latest', '2025-09-30 14:34:23.798');
INSERT INTO public.t_ds_audit_log VALUES (630, 1, -1, 'file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar', 'File', 'Delete', 'DELETE_RESOURCE_BY_ID_NOTES', 45, NULL, '2025-09-30 14:43:02.976');
INSERT INTO public.t_ds_audit_log VALUES (631, 1, -1, 'qdata-etl-3.8.8.jar', 'File', 'Create', 'CREATE_RESOURCE_NOTES', 11330, NULL, '2025-09-30 14:43:18.481');
INSERT INTO public.t_ds_audit_log VALUES (632, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 44, 'latest', '2025-09-30 14:43:47.306');
INSERT INTO public.t_ds_audit_log VALUES (633, 1, 137, '转化_水位预警等级校验-8-20250930143424593', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 273, NULL, '2025-09-30 14:43:59.363');
INSERT INTO public.t_ds_audit_log VALUES (634, 1, 103, '作业_水位预警计算_串行-2-20250923101026092', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 273, NULL, '2025-09-30 14:43:59.363');
INSERT INTO public.t_ds_audit_log VALUES (635, 1, 101, 'Flink流_水位信息采集-31-20250922174347620', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 273, NULL, '2025-09-30 14:43:59.363');
INSERT INTO public.t_ds_audit_log VALUES (636, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 140, NULL, '2025-09-30 14:44:47.543');
INSERT INTO public.t_ds_audit_log VALUES (637, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Schedule', 'Offline', 'OFFLINE_SCHEDULE_NOTES', 13, '8', '2025-09-30 14:44:47.705');
INSERT INTO public.t_ds_audit_log VALUES (638, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-30 14:44:53.702');
INSERT INTO public.t_ds_audit_log VALUES (639, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-30 14:44:56.465');
INSERT INTO public.t_ds_audit_log VALUES (640, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-30 14:44:58.242');
INSERT INTO public.t_ds_audit_log VALUES (641, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-30 14:44:59.971');
INSERT INTO public.t_ds_audit_log VALUES (642, 1, 152414754586944, '站点编码非空与格式稽查dmzss461758512579263', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 168, NULL, '2025-09-30 14:45:03.287');
INSERT INTO public.t_ds_audit_log VALUES (643, 1, 152414792195392, '水位数值范围稽查kv2v2hu1758598123009', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 65, NULL, '2025-09-30 14:45:06.088');
INSERT INTO public.t_ds_audit_log VALUES (644, 1, 152650261388608, '数据完整性稽查oJLrLtP1758598124774', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 75, NULL, '2025-09-30 14:45:08.903');
INSERT INTO public.t_ds_audit_log VALUES (645, 1, 152648830397760, '站点经纬度范围与精度稽查j5eQpn31758512584534', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 76, NULL, '2025-09-30 14:45:12.103');
INSERT INTO public.t_ds_audit_log VALUES (646, 1, 133155949418208, '千数平台', 'Environment', 'Update', 'UPDATE_ENVIRONMENT_NOTES', 64, NULL, '2025-09-30 14:45:29.547');
INSERT INTO public.t_ds_audit_log VALUES (647, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 18, 'latest', '2025-09-30 14:45:35.279');
INSERT INTO public.t_ds_audit_log VALUES (648, 1, 152648200553792, '水位重复记录稽查3PUdWek1758598136729', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 75, NULL, '2025-09-30 14:45:46.978');
INSERT INTO public.t_ds_audit_log VALUES (649, 1, 133155949418208, '千数平台', 'Environment', 'Update', 'UPDATE_ENVIRONMENT_NOTES', 6, NULL, '2025-09-30 14:49:14.003');
INSERT INTO public.t_ds_audit_log VALUES (650, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-30 14:49:17.98');
INSERT INTO public.t_ds_audit_log VALUES (651, 1, 139, '转化_水位预警等级校验-8-20250930144535368', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-30 14:49:24.779');
INSERT INTO public.t_ds_audit_log VALUES (652, 1, 138, '转化_水位预警等级校验-8-20250930144347901', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-30 14:49:24.779');
INSERT INTO public.t_ds_audit_log VALUES (653, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-30 14:51:26.738');
INSERT INTO public.t_ds_audit_log VALUES (654, 1, 140, '转化_水位预警等级校验-8-20250930144918322', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 95, NULL, '2025-09-30 14:52:19.467');
INSERT INTO public.t_ds_audit_log VALUES (656, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 15, NULL, '2025-09-30 14:52:37.018');
INSERT INTO public.t_ds_audit_log VALUES (658, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Schedule', 'Online', 'ONLINE_SCHEDULE_NOTES', 104, '16', '2025-09-30 14:52:37.216');
INSERT INTO public.t_ds_audit_log VALUES (655, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 177, NULL, '2025-09-30 14:52:36.826');
INSERT INTO public.t_ds_audit_log VALUES (657, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 123, '16', '2025-09-30 14:52:37.06');
INSERT INTO public.t_ds_audit_log VALUES (659, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 26, 'latest', '2025-09-30 14:52:38.907');
INSERT INTO public.t_ds_audit_log VALUES (660, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 20, 'latest', '2025-09-30 14:56:45.158');
INSERT INTO public.t_ds_audit_log VALUES (661, 1, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 45, NULL, '2025-09-30 14:56:53.376');
INSERT INTO public.t_ds_audit_log VALUES (662, 1, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 14:56:53.449');
INSERT INTO public.t_ds_audit_log VALUES (663, 1, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 22, '17', '2025-09-30 14:56:53.474');
INSERT INTO public.t_ds_audit_log VALUES (664, 1, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-30 14:56:57.321');
INSERT INTO public.t_ds_audit_log VALUES (665, 1, 141, '转化_水位预警等级校验-8-20250930145126843', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-30 14:57:01.065');
INSERT INTO public.t_ds_audit_log VALUES (666, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 57, NULL, '2025-09-30 15:20:01.236');
INSERT INTO public.t_ds_audit_log VALUES (667, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 146, NULL, '2025-09-30 15:20:02.879');
INSERT INTO public.t_ds_audit_log VALUES (668, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-30 15:20:05.264');
INSERT INTO public.t_ds_audit_log VALUES (669, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 33, 'latest', '2025-09-30 15:20:11.461');
INSERT INTO public.t_ds_audit_log VALUES (670, 1, 143, '转化_水位预警等级校验-8-20250930145645868', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 15, NULL, '2025-09-30 15:20:15.083');
INSERT INTO public.t_ds_audit_log VALUES (671, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:21:59.391');
INSERT INTO public.t_ds_audit_log VALUES (672, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 60, NULL, '2025-09-30 15:22:37.553');
INSERT INTO public.t_ds_audit_log VALUES (673, 1, 152642396472640, '转化_水位列转行', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-30 15:22:42.551');
INSERT INTO public.t_ds_audit_log VALUES (674, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 82, NULL, '2025-09-30 15:26:33.685');
INSERT INTO public.t_ds_audit_log VALUES (675, 1, 152439162752320, '转化_水位预警等级校验', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-30 15:26:36.253');
INSERT INTO public.t_ds_audit_log VALUES (676, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-30 15:26:37.354');
INSERT INTO public.t_ds_audit_log VALUES (677, 1, 152642396472640, '转化_水位列转行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 102, 'latest', '2025-09-30 15:26:38.471');
INSERT INTO public.t_ds_audit_log VALUES (678, 1, 145, '转化_水位预警等级校验-9-20250930152012203', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 28, NULL, '2025-09-30 15:26:42.621');
INSERT INTO public.t_ds_audit_log VALUES (679, 1, 152648927087936, '转化_水位行转列', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:26:55.711');
INSERT INTO public.t_ds_audit_log VALUES (680, 1, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-30 15:30:35.547');
INSERT INTO public.t_ds_audit_log VALUES (681, 1, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:30:35.582');
INSERT INTO public.t_ds_audit_log VALUES (682, 1, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'Schedule', 'Create', 'CREATE_SCHEDULE_NOTES', 17, '18', '2025-09-30 15:30:35.599');
INSERT INTO public.t_ds_audit_log VALUES (683, 1, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 12, 'latest', '2025-09-30 15:30:37.291');
INSERT INTO public.t_ds_audit_log VALUES (684, 1, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 15:30:39.823');
INSERT INTO public.t_ds_audit_log VALUES (685, 1, 152642396472640, '转化_水位列转行', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:32:06.087');
INSERT INTO public.t_ds_audit_log VALUES (686, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 69, NULL, '2025-09-30 15:32:21.21');
INSERT INTO public.t_ds_audit_log VALUES (687, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:32:22.786');
INSERT INTO public.t_ds_audit_log VALUES (688, 1, 152642396472640, '转化_水位列转行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 13, 'latest', '2025-09-30 15:32:24.062');
INSERT INTO public.t_ds_audit_log VALUES (689, 1, 146, '转化_水位列转行-4-20250930152638981', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-30 15:32:28.538');
INSERT INTO public.t_ds_audit_log VALUES (690, 1, 152642396472640, '转化_水位列转行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-30 15:35:00.448');
INSERT INTO public.t_ds_audit_log VALUES (691, 1, 149, '转化_水位列转行-5-20250930153224739', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-30 15:35:06.613');
INSERT INTO public.t_ds_audit_log VALUES (692, 1, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-30 15:35:17.307');
INSERT INTO public.t_ds_audit_log VALUES (693, 1, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 69, NULL, '2025-09-30 15:36:00.235');
INSERT INTO public.t_ds_audit_log VALUES (694, 1, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:36:00.315');
INSERT INTO public.t_ds_audit_log VALUES (695, 1, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 'Schedule', 'Update', 'UPDATE_SCHEDULE_NOTES', 15, '17', '2025-09-30 15:36:00.362');
INSERT INTO public.t_ds_audit_log VALUES (696, 1, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 14, 'latest', '2025-09-30 15:36:05.173');
INSERT INTO public.t_ds_audit_log VALUES (697, 1, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 13, NULL, '2025-09-30 15:36:07.976');
INSERT INTO public.t_ds_audit_log VALUES (698, 1, 152642396472640, '转化_水位列转行', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:36:25.091');
INSERT INTO public.t_ds_audit_log VALUES (699, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 47, NULL, '2025-09-30 15:36:29.83');
INSERT INTO public.t_ds_audit_log VALUES (700, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 15:36:31.652');
INSERT INTO public.t_ds_audit_log VALUES (701, 1, 152642396472640, '转化_水位列转行', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 23, 'latest', '2025-09-30 15:36:32.991');
INSERT INTO public.t_ds_audit_log VALUES (702, 1, 150, '转化_水位列转行-5-20250930153500932', 'ProcessInstance', 'Delete', 'DELETE_PROCESS_INSTANCE_BY_ID_NOTES', 13, NULL, '2025-09-30 15:36:40.195');
INSERT INTO public.t_ds_audit_log VALUES (703, 1, 152642396472640, '转化_水位列转行', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:37:44.841');
INSERT INTO public.t_ds_audit_log VALUES (704, 1, 152642396472640, '转化_排序记录', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 27, NULL, '2025-09-30 15:38:05.922');
INSERT INTO public.t_ds_audit_log VALUES (705, 1, 152642396472640, '转化_水位列转行', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-30 15:38:29.535');
INSERT INTO public.t_ds_audit_log VALUES (706, 1, 152648927087936, '转化_水位行转列', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 31, NULL, '2025-09-30 15:39:23.594');
INSERT INTO public.t_ds_audit_log VALUES (707, 1, 152648927087936, '转化_水位行转列', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:39:25.817');
INSERT INTO public.t_ds_audit_log VALUES (708, 1, 152642396472640, '转化_水位列转行', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:39:27.043');
INSERT INTO public.t_ds_audit_log VALUES (710, 1, 152, '转化_水位列转行-6-20250930153633966', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 12, NULL, '2025-09-30 15:39:36.022');
INSERT INTO public.t_ds_audit_log VALUES (712, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 30, NULL, '2025-09-30 15:41:18.318');
INSERT INTO public.t_ds_audit_log VALUES (714, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-30 15:41:22.04');
INSERT INTO public.t_ds_audit_log VALUES (719, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:44:09.501');
INSERT INTO public.t_ds_audit_log VALUES (728, 1, 152655297809728, '多表_全量写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 31, NULL, '2025-09-30 15:47:08.539');
INSERT INTO public.t_ds_audit_log VALUES (730, 1, 152655297809728, '多表_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-30 15:47:15.71');
INSERT INTO public.t_ds_audit_log VALUES (731, 1, 152655297809728, '多表_全量写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 35, NULL, '2025-09-30 15:47:19.492');
INSERT INTO public.t_ds_audit_log VALUES (732, 1, 152655297809728, '多表_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:47:21.206');
INSERT INTO public.t_ds_audit_log VALUES (733, 1, 152655297809728, '多表_全量写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 15:47:22.456');
INSERT INTO public.t_ds_audit_log VALUES (734, 1, 156, '多表_更新写-3-20250930154537116', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 8, NULL, '2025-09-30 15:47:28.96');
INSERT INTO public.t_ds_audit_log VALUES (711, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 22, NULL, '2025-09-30 15:40:45.997');
INSERT INTO public.t_ds_audit_log VALUES (713, 1, 152649931035968, '转化_站点表字符串拼接', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 15:41:20.391');
INSERT INTO public.t_ds_audit_log VALUES (715, 1, 153, '转化_水位行转列-3-20250930153929910', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 18, NULL, '2025-09-30 15:41:42.479');
INSERT INTO public.t_ds_audit_log VALUES (727, 1, 152655297809728, '多表_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:46:47.148');
INSERT INTO public.t_ds_audit_log VALUES (716, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:42:26.173');
INSERT INTO public.t_ds_audit_log VALUES (723, 1, 152654997830976, '多表_更新写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 65, NULL, '2025-09-30 15:45:32.478');
INSERT INTO public.t_ds_audit_log VALUES (725, 1, 152654997830976, '多表_更新写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 15:45:36.674');
INSERT INTO public.t_ds_audit_log VALUES (726, 1, 155, '转化_站点数据去重-3-20250930154411965', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 14, NULL, '2025-09-30 15:45:41.39');
INSERT INTO public.t_ds_audit_log VALUES (717, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 53, NULL, '2025-09-30 15:43:52.583');
INSERT INTO public.t_ds_audit_log VALUES (718, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 38, NULL, '2025-09-30 15:44:07.444');
INSERT INTO public.t_ds_audit_log VALUES (720, 1, 152653935311168, '转化_站点数据去重', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 15:44:11.391');
INSERT INTO public.t_ds_audit_log VALUES (721, 1, 154, '转化_站点表字符串拼接-2-20250930154122681', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 9, NULL, '2025-09-30 15:44:14.553');
INSERT INTO public.t_ds_audit_log VALUES (722, 1, 152654997830976, '多表_更新写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:45:06.638');
INSERT INTO public.t_ds_audit_log VALUES (724, 1, 152654997830976, '多表_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:45:34.43');
INSERT INTO public.t_ds_audit_log VALUES (729, 1, 152655297809728, '多表_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:47:11.732');
INSERT INTO public.t_ds_audit_log VALUES (735, 1, 152655501885760, '多表_追加写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:48:18.67');
INSERT INTO public.t_ds_audit_log VALUES (736, 1, 152655501885760, '多表_追加写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 42, NULL, '2025-09-30 15:48:35.636');
INSERT INTO public.t_ds_audit_log VALUES (737, 1, 152655501885760, '多表_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 15:48:38.419');
INSERT INTO public.t_ds_audit_log VALUES (738, 1, 152655501885760, '多表_追加写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 15:48:40.661');
INSERT INTO public.t_ds_audit_log VALUES (739, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:48:46.552');
INSERT INTO public.t_ds_audit_log VALUES (740, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:48:48.856');
INSERT INTO public.t_ds_audit_log VALUES (741, 1, 152657219441984, '清洗_水位格式标准化', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:48:50.303');
INSERT INTO public.t_ds_audit_log VALUES (742, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 6, NULL, '2025-09-30 15:48:51.332');
INSERT INTO public.t_ds_audit_log VALUES (743, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-30 15:48:52.449');
INSERT INTO public.t_ds_audit_log VALUES (744, 1, 157, '多表_全量写-3-20250930154722679', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 8, NULL, '2025-09-30 15:48:56.337');
INSERT INTO public.t_ds_audit_log VALUES (745, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 44, NULL, '2025-09-30 15:50:31.828');
INSERT INTO public.t_ds_audit_log VALUES (746, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-30 15:50:38.648');
INSERT INTO public.t_ds_audit_log VALUES (747, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 11, NULL, '2025-09-30 15:50:40.829');
INSERT INTO public.t_ds_audit_log VALUES (748, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:50:43.778');
INSERT INTO public.t_ds_audit_log VALUES (749, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-30 15:50:50.083');
INSERT INTO public.t_ds_audit_log VALUES (750, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 15:50:51.519');
INSERT INTO public.t_ds_audit_log VALUES (751, 1, 152656044221760, '单表_全量读_全量写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 16, 'latest', '2025-09-30 15:50:52.689');
INSERT INTO public.t_ds_audit_log VALUES (752, 1, 158, '多表_追加写-2-20250930154841330', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 13, NULL, '2025-09-30 15:51:13.357');
INSERT INTO public.t_ds_audit_log VALUES (753, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 3, NULL, '2025-09-30 15:53:36.061');
INSERT INTO public.t_ds_audit_log VALUES (754, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-30 15:53:37.83');
INSERT INTO public.t_ds_audit_log VALUES (755, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 39, NULL, '2025-09-30 15:55:28.744');
INSERT INTO public.t_ds_audit_log VALUES (756, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 10, NULL, '2025-09-30 15:55:30.51');
INSERT INTO public.t_ds_audit_log VALUES (757, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 10, 'latest', '2025-09-30 15:55:31.842');
INSERT INTO public.t_ds_audit_log VALUES (758, 1, 159, '单表_全量读_全量写-4-20250930155053237', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 8, NULL, '2025-09-30 15:55:37.327');
INSERT INTO public.t_ds_audit_log VALUES (759, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 8, NULL, '2025-09-30 15:56:20.953');
INSERT INTO public.t_ds_audit_log VALUES (760, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 44, NULL, '2025-09-30 15:56:31.272');
INSERT INTO public.t_ds_audit_log VALUES (761, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-30 15:57:29.416');
INSERT INTO public.t_ds_audit_log VALUES (762, 1, 152656278456640, '单表_时间增量读_更新写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 15:57:33.258');
INSERT INTO public.t_ds_audit_log VALUES (763, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 23, NULL, '2025-09-30 15:57:39.247');
INSERT INTO public.t_ds_audit_log VALUES (764, 1, 152656664685888, '单表_ID增量读_追加写', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 9, 'latest', '2025-09-30 15:57:44.085');
INSERT INTO public.t_ds_audit_log VALUES (765, 1, 160, '单表_时间增量读_更新写-2-20250930155532450', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 10, NULL, '2025-09-30 15:57:56.361');
INSERT INTO public.t_ds_audit_log VALUES (766, 1, 152657219441984, '清洗_水位值边界调整', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 46, NULL, '2025-09-30 16:00:15.138');
INSERT INTO public.t_ds_audit_log VALUES (767, 1, 152657219441984, '清洗_水位值边界调整', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 16:00:20.871');
INSERT INTO public.t_ds_audit_log VALUES (768, 1, 152657219441984, '清洗_水位值边界调整', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 20, 'latest', '2025-09-30 16:00:21.986');
INSERT INTO public.t_ds_audit_log VALUES (769, 1, 161, '单表_ID增量读_追加写-2-20250930155745071', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 12, NULL, '2025-09-30 16:00:32.229');
INSERT INTO public.t_ds_audit_log VALUES (770, 1, 152658250428736, '清洗_水位添加单位', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 36, NULL, '2025-09-30 16:03:07.095');
INSERT INTO public.t_ds_audit_log VALUES (771, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 29, NULL, '2025-09-30 16:03:29.956');
INSERT INTO public.t_ds_audit_log VALUES (772, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 16:03:31.524');
INSERT INTO public.t_ds_audit_log VALUES (773, 1, 152658250428736, '清洗_水位缺失补全', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 8, 'latest', '2025-09-30 16:03:32.47');
INSERT INTO public.t_ds_audit_log VALUES (774, 1, 162, '清洗_水位值边界调整-3-20250930160022831', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 7, NULL, '2025-09-30 16:03:34.659');
INSERT INTO public.t_ds_audit_log VALUES (775, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 39, NULL, '2025-09-30 16:05:39.342');
INSERT INTO public.t_ds_audit_log VALUES (776, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 7, NULL, '2025-09-30 16:05:43.246');
INSERT INTO public.t_ds_audit_log VALUES (777, 1, 152658885689664, '清洗_水位异常值处理', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 16:05:44.503');
INSERT INTO public.t_ds_audit_log VALUES (778, 1, 163, '清洗_水位缺失补全-4-20250930160332719', 'ProcessInstance', 'BatchDelete', 'BATCH_DELETE_PROCESS_INSTANCE_BY_IDS_NOTES', 11, NULL, '2025-09-30 16:05:55.178');
INSERT INTO public.t_ds_audit_log VALUES (779, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 9, NULL, '2025-09-30 16:06:45.041');
INSERT INTO public.t_ds_audit_log VALUES (780, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 12, NULL, '2025-09-30 16:06:47.288');
INSERT INTO public.t_ds_audit_log VALUES (781, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 5, NULL, '2025-09-30 16:06:54.661');
INSERT INTO public.t_ds_audit_log VALUES (782, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-30 16:06:55.63');
INSERT INTO public.t_ds_audit_log VALUES (783, 1, 152726374059328, 'SparkSQL_水位日统计报表', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 4, NULL, '2025-09-30 16:06:59.688');
INSERT INTO public.t_ds_audit_log VALUES (784, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Offline', 'RELEASE_PROCESS_DEFINITION_NOTES', 3, NULL, '2025-09-30 16:07:00.901');
INSERT INTO public.t_ds_audit_log VALUES (785, 1, 152659477902656, 'Flink流_水位信息采集', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 364, NULL, '2025-09-30 16:07:02.701');
INSERT INTO public.t_ds_audit_log VALUES (786, 1, 152660481929536, 'Flink批_水位信息采集', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 32, NULL, '2025-09-30 16:07:05.301');
INSERT INTO public.t_ds_audit_log VALUES (787, 1, 152726374059328, 'SparkSQL_水位日统计报表', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 38, NULL, '2025-09-30 16:07:07.358');
INSERT INTO public.t_ds_audit_log VALUES (788, 1, 152671899571520, '达梦_水位日统计', 'Process', 'Delete', 'DELETE_PROCESS_DEFINITION_BY_ID_NOTES', 31, NULL, '2025-09-30 16:07:09.425');
INSERT INTO public.t_ds_audit_log VALUES (789, 1, 153383838261792, 'Kingbase_水位日统计', 'Process', 'Create', 'CREATE_PROCESS_DEFINITION_NOTES', 197, NULL, '2025-09-30 16:08:33.849');
INSERT INTO public.t_ds_audit_log VALUES (790, 1, 152672766952768, 'MySQL_水位日统计', 'Process', 'Start', 'RUN_PROCESS_INSTANCE_NOTES', 11, 'latest', '2025-09-30 16:08:53.713');
INSERT INTO public.t_ds_audit_log VALUES (791, 1, 153383838261792, 'Kingbase_水位日统计', 'Process', 'Update', 'UPDATE_PROCESS_DEFINITION_NOTES', 26, NULL, '2025-09-30 16:09:14.421');
INSERT INTO public.t_ds_audit_log VALUES (792, 1, 153383838261792, 'Kingbase_水位日统计', 'Process', 'Online', 'RELEASE_PROCESS_DEFINITION_NOTES', 16, NULL, '2025-09-30 16:09:21.977');


--
-- Data for Name: t_ds_cluster; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_command; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_datasource; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_dq_comparison_type; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_dq_comparison_type VALUES (1, 'FixValue', NULL, NULL, NULL, '2021-06-30 00:00:00', '2021-06-30 00:00:00', false);
INSERT INTO public.t_ds_dq_comparison_type VALUES (2, 'DailyAvg', 'select round(avg(statistics_value),2) as day_avg from t_ds_dq_task_statistics_value where data_time >=date_trunc(''DAY'', ${data_time}) and data_time < date_add(date_trunc(''day'', ${data_time}),1) and unique_code = ${unique_code} and statistics_name = ''${statistics_name}''', 'day_range', 'day_range.day_avg', '2021-06-30 00:00:00', '2021-06-30 00:00:00', true);
INSERT INTO public.t_ds_dq_comparison_type VALUES (3, 'WeeklyAvg', 'select round(avg(statistics_value),2) as week_avg from t_ds_dq_task_statistics_value where  data_time >= date_trunc(''WEEK'', ${data_time}) and data_time <date_trunc(''day'', ${data_time}) and unique_code = ${unique_code} and statistics_name = ''${statistics_name}''', 'week_range', 'week_range.week_avg', '2021-06-30 00:00:00', '2021-06-30 00:00:00', true);
INSERT INTO public.t_ds_dq_comparison_type VALUES (4, 'MonthlyAvg', 'select round(avg(statistics_value),2) as month_avg from t_ds_dq_task_statistics_value where  data_time >= date_trunc(''MONTH'', ${data_time}) and data_time <date_trunc(''day'', ${data_time}) and unique_code = ${unique_code} and statistics_name = ''${statistics_name}''', 'month_range', 'month_range.month_avg', '2021-06-30 00:00:00', '2021-06-30 00:00:00', true);
INSERT INTO public.t_ds_dq_comparison_type VALUES (5, 'Last7DayAvg', 'select round(avg(statistics_value),2) as last_7_avg from t_ds_dq_task_statistics_value where  data_time >= date_add(date_trunc(''day'', ${data_time}),-7) and  data_time <date_trunc(''day'', ${data_time}) and unique_code = ${unique_code} and statistics_name = ''${statistics_name}''', 'last_seven_days', 'last_seven_days.last_7_avg', '2021-06-30 00:00:00', '2021-06-30 00:00:00', true);
INSERT INTO public.t_ds_dq_comparison_type VALUES (6, 'Last30DayAvg', 'select round(avg(statistics_value),2) as last_30_avg from t_ds_dq_task_statistics_value where  data_time >= date_add(date_trunc(''day'', ${data_time}),-30) and  data_time < date_trunc(''day'', ${data_time}) and unique_code = ${unique_code} and statistics_name = ''${statistics_name}''', 'last_thirty_days', 'last_thirty_days.last_30_avg', '2021-06-30 00:00:00', '2021-06-30 00:00:00', true);
INSERT INTO public.t_ds_dq_comparison_type VALUES (7, 'SrcTableTotalRows', 'SELECT COUNT(*) AS total FROM ${src_table} WHERE (${src_filter})', 'total_count', 'total_count.total', '2021-06-30 00:00:00', '2021-06-30 00:00:00', false);
INSERT INTO public.t_ds_dq_comparison_type VALUES (8, 'TargetTableTotalRows', 'SELECT COUNT(*) AS total FROM ${target_table} WHERE (${target_filter})', 'total_count', 'total_count.total', '2021-06-30 00:00:00', '2021-06-30 00:00:00', false);


--
-- Data for Name: t_ds_dq_execute_result; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_dq_rule; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_dq_rule VALUES (1, '$t(null_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (2, '$t(custom_sql)', 1, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (3, '$t(multi_table_accuracy)', 2, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (4, '$t(multi_table_value_comparison)', 3, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (5, '$t(field_length_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (6, '$t(uniqueness_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (7, '$t(regexp_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (8, '$t(timeliness_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (9, '$t(enumeration_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');
INSERT INTO public.t_ds_dq_rule VALUES (10, '$t(table_count_check)', 0, 1, '2020-01-12 00:00:00', '2020-01-12 00:00:00');


--
-- Data for Name: t_ds_dq_rule_execute_sql; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (1, 1, 'SELECT COUNT(*) AS nulls FROM null_items', 'null_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (2, 1, 'SELECT COUNT(*) AS total FROM ${src_table} WHERE (${src_filter})', 'total_count', 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (3, 1, 'SELECT COUNT(*) AS miss from miss_items', 'miss_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (4, 1, 'SELECT COUNT(*) AS valids FROM invalid_length_items', 'invalid_length_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (5, 1, 'SELECT COUNT(*) AS total FROM ${target_table} WHERE (${target_filter})', 'total_count', 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (6, 1, 'SELECT ${src_field} FROM ${src_table} group by ${src_field} having count(*) > 1', 'duplicate_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (7, 1, 'SELECT COUNT(*) AS duplicates FROM duplicate_items', 'duplicate_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (8, 1, 'SELECT ${src_table}.* FROM (SELECT * FROM ${src_table} WHERE (${src_filter})) ${src_table} LEFT JOIN (SELECT * FROM ${target_table} WHERE (${target_filter})) ${target_table} ON ${on_clause} WHERE ${where_clause}', 'miss_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (9, 1, 'SELECT * FROM ${src_table} WHERE (${src_field} not regexp ''${regexp_pattern}'') AND (${src_filter}) ', 'regexp_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (10, 1, 'SELECT COUNT(*) AS regexps FROM regexp_items', 'regexp_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (11, 1, 'SELECT * FROM ${src_table} WHERE (to_unix_timestamp(${src_field}, ''${datetime_format}'')-to_unix_timestamp(''${deadline}'', ''${datetime_format}'') <= 0) AND (to_unix_timestamp(${src_field}, ''${datetime_format}'')-to_unix_timestamp(''${begin_time}'', ''${datetime_format}'') >= 0) AND (${src_filter}) ', 'timeliness_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (12, 1, 'SELECT COUNT(*) AS timeliness FROM timeliness_items', 'timeliness_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (13, 1, 'SELECT * FROM ${src_table} where (${src_field} not in ( ${enum_list} ) or ${src_field} is null) AND (${src_filter}) ', 'enum_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (14, 1, 'SELECT COUNT(*) AS enums FROM enum_items', 'enum_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (15, 1, 'SELECT COUNT(*) AS total FROM ${src_table} WHERE (${src_filter})', 'table_count', 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24', false);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (16, 1, 'SELECT * FROM ${src_table} WHERE (${src_field} is null or ${src_field} = '''') AND (${src_filter})', 'null_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);
INSERT INTO public.t_ds_dq_rule_execute_sql VALUES (17, 1, 'SELECT * FROM ${src_table} WHERE (length(${src_field}) ${logic_operator} ${field_length}) AND (${src_filter})', 'invalid_length_items', 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24', true);


--
-- Data for Name: t_ds_dq_rule_input_entry; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_dq_rule_input_entry VALUES (1, 'src_connector_type', 'select', '$t(src_connector_type)', '', '[{"label":"HIVE","value":"HIVE"},{"label":"JDBC","value":"JDBC"}]', 'please select source connector type', 2, 2, 0, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (2, 'src_datasource_id', 'select', '$t(src_datasource_id)', '', NULL, 'please select source datasource id', 1, 2, 0, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (3, 'src_table', 'select', '$t(src_table)', NULL, NULL, 'Please enter source table name', 0, 0, 0, 1, 1, 1, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (4, 'src_filter', 'input', '$t(src_filter)', NULL, NULL, 'Please enter filter expression', 0, 3, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (5, 'src_field', 'select', '$t(src_field)', NULL, NULL, 'Please enter column, only single column is supported', 0, 0, 0, 1, 1, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (6, 'statistics_name', 'input', '$t(statistics_name)', NULL, NULL, 'Please enter statistics name, the alias in statistics execute sql', 0, 0, 1, 0, 0, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (7, 'check_type', 'select', '$t(check_type)', '0', '[{"label":"Expected - Actual","value":"0"},{"label":"Actual - Expected","value":"1"},{"label":"Actual / Expected","value":"2"},{"label":"(Expected - Actual) / Expected","value":"3"}]', 'please select check type', 0, 0, 3, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (8, 'operator', 'select', '$t(operator)', '0', '[{"label":"=","value":"0"},{"label":"<","value":"1"},{"label":"<=","value":"2"},{"label":">","value":"3"},{"label":">=","value":"4"},{"label":"!=","value":"5"}]', 'please select operator', 0, 0, 3, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (9, 'threshold', 'input', '$t(threshold)', NULL, NULL, 'Please enter threshold, number is needed', 0, 2, 3, 1, 1, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (10, 'failure_strategy', 'select', '$t(failure_strategy)', '0', '[{"label":"Alert","value":"0"},{"label":"Block","value":"1"}]', 'please select failure strategy', 0, 0, 3, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (11, 'target_connector_type', 'select', '$t(target_connector_type)', '', '[{"label":"HIVE","value":"HIVE"},{"label":"JDBC","value":"JDBC"}]', 'Please select target connector type', 2, 0, 0, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (12, 'target_datasource_id', 'select', '$t(target_datasource_id)', '', NULL, 'Please select target datasource', 1, 2, 0, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (13, 'target_table', 'select', '$t(target_table)', NULL, NULL, 'Please enter target table', 0, 0, 0, 1, 1, 1, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (14, 'target_filter', 'input', '$t(target_filter)', NULL, NULL, 'Please enter target filter expression', 0, 3, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (15, 'mapping_columns', 'group', '$t(mapping_columns)', NULL, '[{"field":"src_field","props":{"placeholder":"Please input src field","rows":0,"disabled":false,"size":"small"},"type":"input","title":"src_field"},{"field":"operator","props":{"placeholder":"Please input operator","rows":0,"disabled":false,"size":"small"},"type":"input","title":"operator"},{"field":"target_field","props":{"placeholder":"Please input target field","rows":0,"disabled":false,"size":"small"},"type":"input","title":"target_field"}]', 'please enter mapping columns', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (16, 'statistics_execute_sql', 'textarea', '$t(statistics_execute_sql)', NULL, NULL, 'Please enter statistics execute sql', 0, 3, 0, 1, 1, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (17, 'comparison_name', 'input', '$t(comparison_name)', NULL, NULL, 'Please enter comparison name, the alias in comparison execute sql', 0, 0, 0, 0, 0, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (18, 'comparison_execute_sql', 'textarea', '$t(comparison_execute_sql)', NULL, NULL, 'Please enter comparison execute sql', 0, 3, 0, 1, 1, 0, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (19, 'comparison_type', 'select', '$t(comparison_type)', '', NULL, 'Please enter comparison title', 3, 0, 2, 1, 0, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (20, 'writer_connector_type', 'select', '$t(writer_connector_type)', '', '[{"label":"MYSQL","value":"0"},{"label":"POSTGRESQL","value":"1"}]', 'please select writer connector type', 0, 2, 0, 1, 1, 1, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (21, 'writer_datasource_id', 'select', '$t(writer_datasource_id)', '', NULL, 'please select writer datasource id', 1, 2, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (22, 'target_field', 'select', '$t(target_field)', NULL, NULL, 'Please enter column, only single column is supported', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (23, 'field_length', 'input', '$t(field_length)', NULL, NULL, 'Please enter length limit', 0, 3, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (24, 'logic_operator', 'select', '$t(logic_operator)', '=', '[{"label":"=","value":"="},{"label":"<","value":"<"},{"label":"<=","value":"<="},{"label":">","value":">"},{"label":">=","value":">="},{"label":"<>","value":"<>"}]', 'please select logic operator', 0, 0, 3, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (25, 'regexp_pattern', 'input', '$t(regexp_pattern)', NULL, NULL, 'Please enter regexp pattern', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (26, 'deadline', 'input', '$t(deadline)', NULL, NULL, 'Please enter deadline', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (27, 'datetime_format', 'input', '$t(datetime_format)', NULL, NULL, 'Please enter datetime format', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (28, 'enum_list', 'input', '$t(enum_list)', NULL, NULL, 'Please enter enumeration', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (29, 'begin_time', 'input', '$t(begin_time)', NULL, NULL, 'Please enter begin time', 0, 0, 0, 1, 1, 0, 0, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (30, 'src_database', 'select', '$t(src_database)', NULL, NULL, 'Please select source database', 0, 0, 0, 1, 1, 1, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_dq_rule_input_entry VALUES (31, 'target_database', 'select', '$t(target_database)', NULL, NULL, 'Please select target database', 0, 0, 0, 1, 1, 1, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');


--
-- Data for Name: t_ds_dq_task_statistics_value; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_environment; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_environment VALUES (1, 133155949418208, '千数平台', 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', '千数平台', 1, '2025-03-04 14:24:13.236', '2025-09-30 14:49:14.005');


--
-- Data for Name: t_ds_environment_worker_group_relation; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_environment_worker_group_relation VALUES (1, 133155949418208, 'default', 1, '2025-06-04 16:13:42.708', '2025-06-04 16:13:42.708');


--
-- Data for Name: t_ds_error_command; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_fav_task; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_k8s; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_k8s_namespace; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_listener_event; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_plugin_define; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_plugin_define VALUES (1, 'Prometheus AlertManager', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input request URL","size":"small"},"field":"url","name":"$t(''url'')","type":"input","title":"$t(''url'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input annotation in json form","size":"small"},"field":"annotations","name":"$t(''annotations'')","type":"input","title":"$t(''annotations'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input Generator URL","size":"small"},"field":"generatorURL","name":"$t(''generatorURL'')","type":"input","title":"$t(''generatorURL'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:27.856', '2025-09-02 11:34:27.856');
INSERT INTO public.t_ds_plugin_define VALUES (2, 'Script', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"the custom parameters passed when calling scripts","size":"small"},"field":"userParams","name":"$t(''userParams'')","type":"input","title":"$t(''userParams'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"the absolute script path under alert-server, and make sure access rights","size":"small"},"field":"path","name":"$t(''scriptPath'')","type":"input","title":"$t(''scriptPath'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"type","name":"$t(''scriptType'')","type":"radio","title":"$t(''scriptType'')","value":"SHELL","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"SHELL","value":"SHELL","disabled":false}]}]', '2025-09-02 11:34:27.916', '2025-09-02 11:34:27.916');
INSERT INTO public.t_ds_plugin_define VALUES (3, 'Telegram', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input WebHook Url","size":"small"},"field":"webHook","name":"$t(''webHook'')","type":"input","title":"$t(''webHook'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input bot access token","size":"small"},"field":"botToken","name":"botToken","type":"input","title":"botToken","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input telegram channel chat id","size":"small"},"field":"chatId","name":"chatId","type":"input","title":"chatId","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"field":"parseMode","name":"parseMode","props":{"disabled":null,"placeholder":null,"size":"small"},"type":"select","title":"parseMode","value":"Txt","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"Txt","value":"Txt","disabled":false},{"label":"Markdown","value":"Markdown","disabled":false},{"label":"MarkdownV2","value":"MarkdownV2","disabled":false},{"label":"Html","value":"Html","disabled":false}]},{"props":null,"field":"IsEnableProxy","name":"$t(''isEnableProxy'')","type":"radio","title":"$t(''isEnableProxy'')","value":"false","validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"Proxy","name":"$t(''proxy'')","type":"input","title":"$t(''proxy'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Port","name":"$t(''port'')","type":"input-number","title":"$t(''port'')","value":null,"validate":[{"required":false,"message":null,"type":"number","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"User","name":"$t(''user'')","type":"input","title":"$t(''user'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":"password","maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"if enable use authentication, you need input password","size":"small"},"field":"Password","name":"$t(''password'')","type":"input","title":"$t(''password'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:27.934', '2025-09-02 11:34:27.934');
INSERT INTO public.t_ds_plugin_define VALUES (4, 'WeChat', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input corp id","size":"small"},"field":"corpId","name":"$t(''corpId'')","type":"input","title":"$t(''corpId'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input secret","size":"small"},"field":"secret","name":"$t(''secret'')","type":"input","title":"$t(''secret'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"use `|` to separate userIds and `@all` to everyone","size":"small"},"field":"users","name":"$t(''users'')","type":"input","title":"$t(''users'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input agent id or chat id","size":"small"},"field":"agentId/chatId","name":"$t(''agentId/chatId'')","type":"input","title":"$t(''agentId/chatId'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"sendType","name":"send.type","type":"radio","title":"send.type","value":"APP/应用","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"APP/应用","value":"APP/应用","disabled":false},{"label":"GROUP CHAT/群聊","value":"GROUP CHAT/群聊","disabled":false}]},{"props":null,"field":"showType","name":"$t(''showType'')","type":"radio","title":"$t(''showType'')","value":"markdown","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"markdown","value":"markdown","disabled":false},{"label":"text","value":"text","disabled":false}]}]', '2025-09-02 11:34:27.951', '2025-09-02 11:34:27.951');
INSERT INTO public.t_ds_plugin_define VALUES (5, 'Email', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input receivers","size":"small"},"field":"receivers","name":"$t(''receivers'')","type":"input","title":"$t(''receivers'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"receiverCcs","name":"$t(''receiverCcs'')","type":"input","title":"$t(''receiverCcs'')","value":null,"validate":null,"emit":null},{"props":null,"field":"serverHost","name":"mail.smtp.host","type":"input","title":"mail.smtp.host","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"serverPort","name":"mail.smtp.port","type":"input-number","title":"mail.smtp.port","value":25,"validate":[{"required":true,"message":null,"type":"number","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"sender","name":"$t(''mailSender'')","type":"input","title":"$t(''mailSender'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"enableSmtpAuth","name":"mail.smtp.auth","type":"radio","title":"mail.smtp.auth","value":"true","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"if enable use authentication, you need input user","size":"small"},"field":"User","name":"$t(''mailUser'')","type":"input","title":"$t(''mailUser'')","value":null,"validate":null,"emit":null},{"props":{"disabled":null,"type":"password","maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"if enable use authentication, you need input password","size":"small"},"field":"Password","name":"$t(''mailPasswd'')","type":"input","title":"$t(''mailPasswd'')","value":null,"validate":null,"emit":null},{"props":null,"field":"starttlsEnable","name":"mail.smtp.starttls.enable","type":"radio","title":"mail.smtp.starttls.enable","value":"false","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"sslEnable","name":"mail.smtp.ssl.enable","type":"radio","title":"mail.smtp.ssl.enable","value":"false","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"smtpSslTrust","name":"mail.smtp.ssl.trust","type":"input","title":"mail.smtp.ssl.trust","value":"*","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"showType","name":"$t(''showType'')","type":"radio","title":"$t(''showType'')","value":"table","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"table","value":"table","disabled":false},{"label":"text","value":"text","disabled":false},{"label":"attachment","value":"attachment","disabled":false},{"label":"table attachment","value":"table attachment","disabled":false}]}]', '2025-09-02 11:34:27.968', '2025-09-02 11:34:27.968');
INSERT INTO public.t_ds_plugin_define VALUES (6, 'AliyunVoice', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input called number","size":"small"},"field":"calledNumber","name":"$t(''calledNumber'')","type":"input","title":"$t(''calledNumber'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"Please enter the call display number (the default number will be used if you do not fill in)","size":"small"},"field":"calledShowNumber","name":"$t(''calledShowNumber'')","type":"input","title":"$t(''calledShowNumber'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input tts code","size":"small"},"field":"ttsCode","name":"$t(''ttsCode'')","type":"input","title":"$t(''ttsCode'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input aliyun viice address","size":"small"},"field":"address","name":"$t(''address'')","type":"input","title":"$t(''address'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input accessKeyId","size":"small"},"field":"accessKeyId","name":"$t(''accessKeyId'')","type":"input","title":"$t(''accessKeyId'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"please input accessKeySecret","size":"small"},"field":"accessKeySecret","name":"$t(''accessKeySecret'')","type":"input","title":"$t(''accessKeySecret'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:27.978', '2025-09-02 11:34:27.978');
INSERT INTO public.t_ds_plugin_define VALUES (31, 'HTTP', 'task', 'null', '2025-09-02 11:34:42.562', '2025-09-02 11:34:42.562');
INSERT INTO public.t_ds_plugin_define VALUES (32, 'DMS', 'task', '[]', '2025-09-02 11:34:42.566', '2025-09-02 11:34:42.566');
INSERT INTO public.t_ds_plugin_define VALUES (33, 'EMR', 'task', '[]', '2025-09-02 11:34:42.571', '2025-09-02 11:34:42.571');
INSERT INTO public.t_ds_plugin_define VALUES (34, 'DATA_QUALITY', 'task', 'null', '2025-09-02 11:34:42.578', '2025-09-02 11:34:42.578');
INSERT INTO public.t_ds_plugin_define VALUES (35, 'KUBEFLOW', 'task', '[]', '2025-09-02 11:34:42.58', '2025-09-02 11:34:42.58');
INSERT INTO public.t_ds_plugin_define VALUES (36, 'SQL', 'task', 'null', '2025-09-02 11:34:42.585', '2025-09-02 11:34:42.585');
INSERT INTO public.t_ds_plugin_define VALUES (37, 'DVC', 'task', '[{"props":null,"field":"name","name":"$t(''Node name'')","type":"input","title":"$t(''Node name'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"runFlag","name":"RUN_FLAG","type":"radio","title":"RUN_FLAG","value":null,"validate":null,"emit":null,"options":[{"label":"NORMAL","value":"NORMAL","disabled":false},{"label":"FORBIDDEN","value":"FORBIDDEN","disabled":false}]}]', '2025-09-02 11:34:42.588', '2025-09-02 11:34:42.588');
INSERT INTO public.t_ds_plugin_define VALUES (38, 'DATAX', 'task', 'null', '2025-09-02 11:34:42.593', '2025-09-02 11:34:42.593');
INSERT INTO public.t_ds_plugin_define VALUES (39, 'ZEPPELIN', 'task', 'null', '2025-09-02 11:34:42.599', '2025-09-02 11:34:42.599');
INSERT INTO public.t_ds_plugin_define VALUES (40, 'DINKY', 'task', '[]', '2025-09-02 11:34:42.602', '2025-09-02 11:34:42.602');
INSERT INTO public.t_ds_plugin_define VALUES (7, 'Slack', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input WebHook Url","size":"small"},"field":"webHook","name":"$t(''webhook'')","type":"input","title":"$t(''webhook'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input the bot username","size":"small"},"field":"username","name":"$t(''Username'')","type":"input","title":"$t(''Username'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:27.987', '2025-09-02 11:34:27.987');
INSERT INTO public.t_ds_plugin_define VALUES (8, 'Feishu', 'alert', '[{"props":null,"field":"WebHook","name":"$t(''webhook'')","type":"input","title":"$t(''webhook'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"IsEnableProxy","name":"$t(''isEnableProxy'')","type":"radio","title":"$t(''isEnableProxy'')","value":"true","validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"Proxy","name":"$t(''proxy'')","type":"input","title":"$t(''proxy'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Port","name":"$t(''port'')","type":"input-number","title":"$t(''port'')","value":null,"validate":[{"required":false,"message":null,"type":"number","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"User","name":"$t(''user'')","type":"input","title":"$t(''user'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":"password","maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"if enable use authentication, you need input password","size":"small"},"field":"Password","name":"$t(''password'')","type":"input","title":"$t(''password'')","value":null,"validate":null,"emit":null}]', '2025-09-02 11:34:27.995', '2025-09-02 11:34:27.995');
INSERT INTO public.t_ds_plugin_define VALUES (9, 'Http', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input request URL","size":"small"},"field":"url","name":"$t(''url'')","type":"input","title":"$t(''url'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input request type POST or GET","size":"small"},"field":"requestType","name":"$t(''requestType'')","type":"input","title":"$t(''requestType'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input request headers as JSON format","size":"small"},"field":"headerParams","name":"$t(''headerParams'')","type":"input","title":"$t(''headerParams'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input request body as JSON format","size":"small"},"field":"bodyParams","name":"$t(''bodyParams'')","type":"input","title":"$t(''bodyParams'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input alert msg field name","size":"small"},"field":"contentField","name":"$t(''contentField'')","type":"input","title":"$t(''contentField'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"timeout","name":"$t(''timeout'')","type":"input-number","title":"$t(''timeout'')","value":120,"validate":[{"required":false,"message":null,"type":"number","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:28.005', '2025-09-02 11:34:28.005');
INSERT INTO public.t_ds_plugin_define VALUES (10, 'DingTalk', 'alert', '[{"props":null,"field":"WebHook","name":"$t(''webhook'')","type":"input","title":"$t(''webhook'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Keyword","name":"$t(''keyword'')","type":"input","title":"$t(''keyword'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Secret","name":"$t(''secret'')","type":"input","title":"$t(''secret'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"MsgType","name":"$t(''msgType'')","type":"radio","title":"$t(''msgType'')","value":"text","validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"text","value":"text","disabled":false},{"label":"markdown","value":"markdown","disabled":false}]},{"props":null,"field":"AtMobiles","name":"$t(''atMobiles'')","type":"input","title":"$t(''atMobiles'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"AtUserIds","name":"$t(''atUserIds'')","type":"input","title":"$t(''atUserIds'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"IsAtAll","name":"$t(''isAtAll'')","type":"radio","title":"$t(''isAtAll'')","value":"false","validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"IsEnableProxy","name":"$t(''isEnableProxy'')","type":"radio","title":"$t(''isEnableProxy'')","value":"false","validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"YES","value":"true","disabled":false},{"label":"NO","value":"false","disabled":false}]},{"props":null,"field":"Proxy","name":"$t(''proxy'')","type":"input","title":"$t(''proxy'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Port","name":"$t(''port'')","type":"input-number","title":"$t(''port'')","value":null,"validate":[{"required":false,"message":null,"type":"number","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"User","name":"$t(''user'')","type":"input","title":"$t(''user'')","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":"password","maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"if enable use authentication, you need input password","size":"small"},"field":"Password","name":"$t(''password'')","type":"input","title":"$t(''password'')","value":null,"validate":null,"emit":null}]', '2025-09-02 11:34:28.015', '2025-09-02 11:34:28.015');
INSERT INTO public.t_ds_plugin_define VALUES (11, 'WebexTeams', 'alert', '[{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input bot access token","size":"small"},"field":"BotAccessToken","name":"botAccessToken","type":"input","title":"botAccessToken","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input the room ID the alert message send to","size":"small"},"field":"RoomId","name":"roomId","type":"input","title":"roomId","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input the person ID of the alert message recipient","size":"small"},"field":"ToPersonId","name":"toPersonId","type":"input","title":"toPersonId","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"input the email address of the alert message recipient","size":"small"},"field":"ToPersonEmail","name":"toPersonEmail","type":"input","title":"toPersonEmail","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":{"disabled":null,"type":null,"maxlength":null,"minlength":null,"clearable":null,"prefixIcon":null,"suffixIcon":null,"rows":null,"autosize":null,"autocomplete":null,"name":null,"readonly":null,"max":null,"min":null,"step":null,"resize":null,"autofocus":null,"form":null,"label":null,"tabindex":null,"validateEvent":null,"showPassword":null,"placeholder":"use `,`(eng commas) to separate multiple emails, to specify the person you mention in the room","size":"small"},"field":"AtSomeoneInRoom","name":"atSomeoneInRoom","type":"input","title":"atSomeoneInRoom","value":null,"validate":[{"required":false,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"Destination","name":"destination","type":"radio","title":"destination","value":"roomId","validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null,"options":[{"label":"roomId","value":"roomId","disabled":false},{"label":"personEmail","value":"personEmail","disabled":false},{"label":"personId","value":"personId","disabled":false}]}]', '2025-09-02 11:34:28.025', '2025-09-02 11:34:28.025');
INSERT INTO public.t_ds_plugin_define VALUES (12, 'PagerDuty', 'alert', '[{"props":null,"field":"IntegrationKey","name":"integrationKey","type":"input","title":"integrationKey","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:28.032', '2025-09-02 11:34:28.032');
INSERT INTO public.t_ds_plugin_define VALUES (13, 'JAVA', 'task', 'null', '2025-09-02 11:34:42.395', '2025-09-02 11:34:42.395');
INSERT INTO public.t_ds_plugin_define VALUES (14, 'JUPYTER', 'task', 'null', '2025-09-02 11:34:42.411', '2025-09-02 11:34:42.411');
INSERT INTO public.t_ds_plugin_define VALUES (15, 'SPARK', 'task', 'null', '2025-09-02 11:34:42.415', '2025-09-02 11:34:42.415');
INSERT INTO public.t_ds_plugin_define VALUES (16, 'FLINK_STREAM', 'task', 'null', '2025-09-02 11:34:42.419', '2025-09-02 11:34:42.419');
INSERT INTO public.t_ds_plugin_define VALUES (17, 'PYTHON', 'task', 'null', '2025-09-02 11:34:42.429', '2025-09-02 11:34:42.429');
INSERT INTO public.t_ds_plugin_define VALUES (18, 'DATASYNC', 'task', '[]', '2025-09-02 11:34:42.443', '2025-09-02 11:34:42.443');
INSERT INTO public.t_ds_plugin_define VALUES (19, 'DATA_FACTORY', 'task', '[]', '2025-09-02 11:34:42.454', '2025-09-02 11:34:42.454');
INSERT INTO public.t_ds_plugin_define VALUES (20, 'CHUNJUN', 'task', 'null', '2025-09-02 11:34:42.464', '2025-09-02 11:34:42.464');
INSERT INTO public.t_ds_plugin_define VALUES (21, 'REMOTESHELL', 'task', '[{"props":null,"field":"name","name":"$t(''Node name'')","type":"input","title":"$t(''Node name'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"runFlag","name":"RUN_FLAG","type":"radio","title":"RUN_FLAG","value":null,"validate":null,"emit":null,"options":[{"label":"NORMAL","value":"NORMAL","disabled":false},{"label":"FORBIDDEN","value":"FORBIDDEN","disabled":false}]}]', '2025-09-02 11:34:42.516', '2025-09-02 11:34:42.516');
INSERT INTO public.t_ds_plugin_define VALUES (22, 'PIGEON', 'task', '[{"props":null,"field":"targetJobName","name":"targetJobName","type":"input","title":"targetJobName","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null}]', '2025-09-02 11:34:42.521', '2025-09-02 11:34:42.521');
INSERT INTO public.t_ds_plugin_define VALUES (23, 'PROCEDURE', 'task', 'null', '2025-09-02 11:34:42.526', '2025-09-02 11:34:42.526');
INSERT INTO public.t_ds_plugin_define VALUES (24, 'SHELL', 'task', '[{"props":null,"field":"name","name":"$t(''Node name'')","type":"input","title":"$t(''Node name'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"runFlag","name":"RUN_FLAG","type":"radio","title":"RUN_FLAG","value":null,"validate":null,"emit":null,"options":[{"label":"NORMAL","value":"NORMAL","disabled":false},{"label":"FORBIDDEN","value":"FORBIDDEN","disabled":false}]}]', '2025-09-02 11:34:42.532', '2025-09-02 11:34:42.532');
INSERT INTO public.t_ds_plugin_define VALUES (25, 'SQOOP', 'task', 'null', '2025-09-02 11:34:42.536', '2025-09-02 11:34:42.536');
INSERT INTO public.t_ds_plugin_define VALUES (26, 'MR', 'task', 'null', '2025-09-02 11:34:42.54', '2025-09-02 11:34:42.54');
INSERT INTO public.t_ds_plugin_define VALUES (27, 'PYTORCH', 'task', '[]', '2025-09-02 11:34:42.544', '2025-09-02 11:34:42.544');
INSERT INTO public.t_ds_plugin_define VALUES (28, 'K8S', 'task', 'null', '2025-09-02 11:34:42.548', '2025-09-02 11:34:42.548');
INSERT INTO public.t_ds_plugin_define VALUES (29, 'SEATUNNEL', 'task', 'null', '2025-09-02 11:34:42.555', '2025-09-02 11:34:42.555');
INSERT INTO public.t_ds_plugin_define VALUES (30, 'SAGEMAKER', 'task', '[]', '2025-09-02 11:34:42.558', '2025-09-02 11:34:42.558');
INSERT INTO public.t_ds_plugin_define VALUES (41, 'MLFLOW', 'task', '[{"props":null,"field":"name","name":"$t(''Node name'')","type":"input","title":"$t(''Node name'')","value":null,"validate":[{"required":true,"message":null,"type":"string","trigger":"blur","min":null,"max":null}],"emit":null},{"props":null,"field":"runFlag","name":"RUN_FLAG","type":"radio","title":"RUN_FLAG","value":null,"validate":null,"emit":null,"options":[{"label":"NORMAL","value":"NORMAL","disabled":false},{"label":"FORBIDDEN","value":"FORBIDDEN","disabled":false}]}]', '2025-09-02 11:34:42.608', '2025-09-02 11:34:42.608');
INSERT INTO public.t_ds_plugin_define VALUES (42, 'OPENMLDB', 'task', 'null', '2025-09-02 11:34:42.613', '2025-09-02 11:34:42.613');
INSERT INTO public.t_ds_plugin_define VALUES (43, 'LINKIS', 'task', 'null', '2025-09-02 11:34:42.617', '2025-09-02 11:34:42.617');
INSERT INTO public.t_ds_plugin_define VALUES (44, 'HIVECLI', 'task', 'null', '2025-09-02 11:34:42.621', '2025-09-02 11:34:42.621');
INSERT INTO public.t_ds_plugin_define VALUES (45, 'FLINK', 'task', 'null', '2025-09-02 11:34:42.627', '2025-09-02 11:34:42.627');


--
-- Data for Name: t_ds_process_definition; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_process_definition VALUES (2, 150911519707360, '测试111', 2, '', 141883958809440, 1, 1, '[]', '[{"taskCode":150911519707360,"x":0,"y":0}]', NULL, 1, 0, 0, '2025-09-02 17:26:48.117', '2025-09-02 17:31:15.666');
INSERT INTO public.t_ds_process_definition VALUES (46, 152150248157120, '232332', 1, '', 141883958809440, 1, 1, '[]', '[{"taskCode":152150248157120,"x":0,"y":0}]', NULL, 1, 0, 0, '2025-09-16 17:24:34.34', '2025-09-16 17:24:34.34');
INSERT INTO public.t_ds_process_definition VALUES (52, 152413381367104, '工程监管数据发现wSAxLkg1758271769213', 2, '', 134799536571008, 1, 1, '[]', '[{"taskCode":"152413553931584","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-19 16:46:32.742', '2025-09-19 16:49:29.247');
INSERT INTO public.t_ds_process_definition VALUES (54, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 1, '', 134799536571008, 1, 1, '[]', '[{"taskCode":"152413555441984","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-19 16:49:30.698', '2025-09-19 16:49:30.698');
INSERT INTO public.t_ds_process_definition VALUES (5, 150982603138816, '测试9LMXA6X1756874378318', 2, '', 147372832245312, 1, 1, '[]', '[{"taskCode":"150982633305856","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-03 12:39:08.862', '2025-09-03 12:39:38.371');
INSERT INTO public.t_ds_process_definition VALUES (55, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 1, '', 134799536571008, 1, 1, '[]', '[{"taskCode":"152413557107008","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-19 16:49:32.327', '2025-09-19 16:49:32.327');
INSERT INTO public.t_ds_process_definition VALUES (47, 152150518585280, '232332Cjy7WIv1758014906806', 2, '', 134799536571008, 1, 1, '[]', '[{"taskCode":"152150534972352","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-16 17:28:10.806', '2025-09-16 17:28:26.846');
INSERT INTO public.t_ds_process_definition VALUES (56, 152413579812160, '水资源数据发现xAxpna41758271796919', 1, '', 134799536571008, 1, 1, '[]', '[{"taskCode":"152413579804992","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-19 16:49:56.929', '2025-09-19 16:49:56.929');
INSERT INTO public.t_ds_process_definition VALUES (49, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 1, '', 147372832245312, 1, 1, '[]', '[{"taskCode":"152150622975936","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-16 17:29:52.755', '2025-09-16 17:29:52.755');
INSERT INTO public.t_ds_process_definition VALUES (50, 152150699767744, '1212', 2, '', 141883958809440, 0, 1, '[]', '[{"taskCode":152150699760576,"x":700,"y":300}]', NULL, 1, 0, 0, '2025-09-16 17:31:07.742', '2025-09-16 17:31:49.223');
INSERT INTO public.t_ds_process_definition VALUES (92, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152657963461952","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-22 13:57:17.76', '2025-09-22 13:57:17.76');
INSERT INTO public.t_ds_process_definition VALUES (59, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 3, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152662937492800","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-19 17:35:06.691', '2025-09-22 15:24:10.635');
INSERT INTO public.t_ds_process_definition VALUES (67, 152642396472640, '转化_水位列转行', 8, '【达梦 →  明细层】将多字段水位统计表排序输出', 152317790975712, 1, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, '2025-09-22 09:25:52.357', '2025-09-30 15:38:29.549');
INSERT INTO public.t_ds_process_definition VALUES (73, 152648927087936, '转化_水位行转列', 3, '【达梦 → 明细层】将逐时水位记录转为列式存储', 152317790975712, 1, 1, '[]', '[{"taskCode":152648927077696,"x":320,"y":150}]', NULL, 1, 0, 0, '2025-09-22 11:19:31.097', '2025-09-30 15:39:23.613');
INSERT INTO public.t_ds_process_definition VALUES (83, 152655297809728, '多表_全量写', 3, '【达梦 → 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 1, 1, '[]', '[{"taskCode":152655297790272,"x":400,"y":130}]', NULL, 1, 0, 0, '2025-09-22 13:10:50.235', '2025-09-30 15:47:19.518');
INSERT INTO public.t_ds_process_definition VALUES (79, 152653935311168, '转化_站点数据去重', 3, '【达梦 →  原始层】对站点水位监测站点数据进行去重处理', 152317790975712, 1, 1, '[]', '[{"taskCode":152653935271232,"x":270,"y":150}]', NULL, 1, 0, 0, '2025-09-22 12:46:47.632', '2025-09-30 15:44:07.47');
INSERT INTO public.t_ds_process_definition VALUES (86, 152656278456640, '单表_时间增量读_更新写', 3, '【达梦 →  原始层】按时间增量采集水位数据，实时更新', 152317790975712, 1, 1, '[]', '[{"taskCode":152656278439232,"x":320,"y":170}]', NULL, 1, 0, 0, '2025-09-22 13:27:45.333', '2025-09-30 15:56:31.3');
INSERT INTO public.t_ds_process_definition VALUES (74, 152649931035968, '转化_站点表字符串拼接', 2, '【达梦 → 明细层】将站点表中的字符串字段进行拼接转换', 152317790975712, 1, 1, '[]', '[{"taskCode":152649930990912,"x":360,"y":230}]', NULL, 1, 0, 0, '2025-09-22 11:37:12.771', '2025-09-30 15:41:18.338');
INSERT INTO public.t_ds_process_definition VALUES (80, 152654997830976, '多表_更新写', 3, '【达梦 →  主题层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 1, 1, '[]', '[{"taskCode":152654997784896,"x":210,"y":160}]', NULL, 1, 0, 0, '2025-09-22 13:05:39.183', '2025-09-30 15:45:32.519');
INSERT INTO public.t_ds_process_definition VALUES (84, 152655501885760, '多表_追加写', 2, '【达梦 → 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 1, 1, '[]', '[{"taskCode":152655501865280,"x":250,"y":190}]', NULL, 1, 0, 0, '2025-09-22 13:14:24.026', '2025-09-30 15:48:35.666');
INSERT INTO public.t_ds_process_definition VALUES (85, 152656044221760, '单表_全量读_全量写', 4, '【达梦 → 原始层】全量读取水位监测表，日全量重建', 152317790975712, 1, 1, '[]', '[{"taskCode":152656044210496,"x":260,"y":170}]', NULL, 1, 0, 0, '2025-09-22 13:23:43.684', '2025-09-30 15:50:50.097');
INSERT INTO public.t_ds_process_definition VALUES (88, 152657219441984, '清洗_水位值边界调整', 3, '【达梦 → 明细层】水位值边界调整', 152317790975712, 1, 1, '[]', '[{"taskCode":152657219431744,"x":280,"y":180}]', NULL, 1, 0, 0, '2025-09-22 13:44:11.848', '2025-09-30 16:00:15.169');
INSERT INTO public.t_ds_process_definition VALUES (87, 152656664685888, '单表_ID增量读_追加写', 2, '【达梦 → 原始层】按 ID 增量采集水位数据，定时追加', 152317790975712, 1, 1, '[]', '[{"taskCode":152656664673600,"x":340,"y":160}]', NULL, 1, 0, 0, '2025-09-22 13:34:27.701', '2025-09-30 15:57:29.434');
INSERT INTO public.t_ds_process_definition VALUES (93, 152658250428736, '清洗_水位缺失补全', 4, '【达梦 →明细层】补全水位单位', 152317790975712, 1, 1, '[]', '[{"taskCode":152658250408256,"x":300,"y":60}]', NULL, 1, 0, 0, '2025-09-22 14:02:24.009', '2025-09-30 16:03:29.977');
INSERT INTO public.t_ds_process_definition VALUES (95, 152658885689664, '清洗_水位异常值处理', 2, '【达梦 → 明细层】清洗水位监测数据，剔除异常值', 152317790975712, 1, 1, '[]', '[{"taskCode":152658885655872,"x":350,"y":140}]', NULL, 1, 0, 0, '2025-09-22 14:13:36.295', '2025-09-30 16:05:39.369');
INSERT INTO public.t_ds_process_definition VALUES (150, 152673403135296, '作业_水位预警计算_串行', 2, '【依次执行】', 152317790975712, 1, 1, '[]', '[{"taskCode":152673322082624,"x":330,"y":120},{"taskCode":152673327374656,"x":330,"y":220},{"taskCode":152673332505920,"x":330,"y":320}]', NULL, 1, 0, 0, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.908');
INSERT INTO public.t_ds_process_definition VALUES (152, 152673582845248, '作业_水位小时均值_并行', 5, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":390,"y":450},{"taskCode":152673488823616,"x":170,"y":120},{"taskCode":152673498672448,"x":170,"y":250},{"taskCode":152673502172480,"x":590,"y":250},{"taskCode":152673511663936,"x":590,"y":120}]', NULL, 1, 0, 0, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.585');
INSERT INTO public.t_ds_process_definition VALUES (145, 152672440882496, 'Hive_水位日统计报表', 2, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 1, 1, '[]', '[{"taskCode":152672440882496,"x":0,"y":0}]', NULL, 1, 0, 0, '2025-09-22 18:03:55.894', '2025-09-22 18:06:41.802');
INSERT INTO public.t_ds_process_definition VALUES (119, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 1, '', 147372832245312, 1, 1, '[]', '[{"taskCode":"152665378277696","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-22 16:07:25.877', '2025-09-22 16:07:25.877');
INSERT INTO public.t_ds_process_definition VALUES (149, 152672766952768, 'MySQL_水位日统计', 1, '【MySQL  存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 1, '[]', '[{"taskCode":152672766952768,"x":0,"y":0}]', NULL, 1, 0, 0, '2025-09-22 18:13:05.613', '2025-09-22 18:13:05.613');
INSERT INTO public.t_ds_process_definition VALUES (61, 152439162752320, '转化_水位预警等级校验', 10, '【达梦 → 应用层】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 1, 1, '[]', '[{"taskCode":152439162287424,"x":280,"y":70}]', NULL, 1, 0, 0, '2025-09-20 00:09:32.174', '2025-09-30 15:22:37.588');
INSERT INTO public.t_ds_process_definition VALUES (193, 153383838261792, 'Kingbase_水位日统计', 2, '【Kingbase 存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 1, '[]', '[{"taskCode":153383838261792,"x":0,"y":0}]', NULL, 1, 0, 0, '2025-09-30 16:08:34.03', '2025-09-30 16:09:14.437');
INSERT INTO public.t_ds_process_definition VALUES (169, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 1, '', 147372832245312, 1, 1, '[]', '[{"taskCode":"153381689437728","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-30 15:30:35.551', '2025-09-30 15:30:35.551');
INSERT INTO public.t_ds_process_definition VALUES (165, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 2, '', 147372832245312, 1, 1, '[]', '[{"taskCode":"153381999104544","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-30 14:56:53.386', '2025-09-30 15:36:00.255');
INSERT INTO public.t_ds_process_definition VALUES (164, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"153379496920608","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, '2025-09-30 14:52:36.949', '2025-09-30 14:52:36.949');


--
-- Data for Name: t_ds_process_definition_log; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_process_definition_log VALUES (2, 150911519707360, '测试111', 1, '', 141883958809440, 0, 1, '[]', '[{"taskCode":150911519707360,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-02 17:26:48.117', '2025-09-02 17:26:48.117', '2025-09-02 17:26:48.117');
INSERT INTO public.t_ds_process_definition_log VALUES (3, 150911519707360, '测试111', 2, '', 141883958809440, 0, 1, '[]', '[{"taskCode":150911519707360,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-02 17:31:15.666', '2025-09-02 17:26:48.117', '2025-09-02 17:31:15.666');
INSERT INTO public.t_ds_process_definition_log VALUES (5, 150982603138816, '测试JEoZH8A1756874348816', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"150982603096832","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-03 12:39:08.862', '2025-09-03 12:39:08.862', '2025-09-03 12:39:08.862');
INSERT INTO public.t_ds_process_definition_log VALUES (6, 150982603138816, '测试9LMXA6X1756874378318', 2, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"150982633305856","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-03 12:39:38.371', '2025-09-03 12:39:08.862', '2025-09-03 12:39:38.371');
INSERT INTO public.t_ds_process_definition_log VALUES (46, 152150248157120, '232332', 1, '', 141883958809440, 0, 1, '[]', '[{"taskCode":152150248157120,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-16 17:24:34.34', '2025-09-16 17:24:34.34', '2025-09-16 17:24:34.34');
INSERT INTO public.t_ds_process_definition_log VALUES (47, 152150518585280, '232332JLZSD3A1758014890777', 1, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152150518559680","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-16 17:28:10.806', '2025-09-16 17:28:10.806', '2025-09-16 17:28:10.806');
INSERT INTO public.t_ds_process_definition_log VALUES (48, 152150518585280, '232332Cjy7WIv1758014906806', 2, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152150534972352","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-16 17:28:26.846', '2025-09-16 17:28:10.806', '2025-09-16 17:28:26.846');
INSERT INTO public.t_ds_process_definition_log VALUES (49, 152150622981056, '2323_user_20250916172917OMdTTDk1758014992747', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152150622975936","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-16 17:29:52.755', '2025-09-16 17:29:52.755', '2025-09-16 17:29:52.755');
INSERT INTO public.t_ds_process_definition_log VALUES (50, 152150699767744, '1212', 1, '', 141883958809440, 0, 1, '[]', '[{"taskCode":152150699760576,"x":700,"y":300}]', NULL, 1, 0, 0, 1, '2025-09-16 17:31:07.742', '2025-09-16 17:31:07.742', '2025-09-16 17:31:07.742');
INSERT INTO public.t_ds_process_definition_log VALUES (51, 152150699767744, '1212', 2, '', 141883958809440, 0, 1, '[]', '[{"taskCode":152150699760576,"x":700,"y":300}]', NULL, 1, 0, 0, 1, '2025-09-16 17:31:49.223', '2025-09-16 17:31:07.742', '2025-09-16 17:31:49.223');
INSERT INTO public.t_ds_process_definition_log VALUES (52, 152413381367104, '工程监管数据发现uVOWKbY1758271591783', 1, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152413380820288","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 16:46:32.742', '2025-09-19 16:46:32.742', '2025-09-19 16:46:32.742');
INSERT INTO public.t_ds_process_definition_log VALUES (53, 152413381367104, '工程监管数据发现wSAxLkg1758271769213', 2, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152413553931584","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 16:49:29.247', '2025-09-19 16:46:32.742', '2025-09-19 16:49:29.247');
INSERT INTO public.t_ds_process_definition_log VALUES (54, 152413555449152, '水文监测数据发现Rg0XxWU1758271770687', 1, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152413555441984","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 16:49:30.698', '2025-09-19 16:49:30.698', '2025-09-19 16:49:30.698');
INSERT INTO public.t_ds_process_definition_log VALUES (55, 152413557117248, '水质监测数据发现OemKK9R1758271772314', 1, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152413557107008","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 16:49:32.327', '2025-09-19 16:49:32.327', '2025-09-19 16:49:32.327');
INSERT INTO public.t_ds_process_definition_log VALUES (56, 152413579812160, '水资源数据发现xAxpna41758271796919', 1, '', 134799536571008, 0, 1, '[]', '[{"taskCode":"152413579804992","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 16:49:56.929', '2025-09-19 16:49:56.929', '2025-09-19 16:49:56.929');
INSERT INTO public.t_ds_process_definition_log VALUES (59, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152416165498176","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 17:35:06.691', '2025-09-19 17:35:06.691', '2025-09-19 17:35:06.691');
INSERT INTO public.t_ds_process_definition_log VALUES (60, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 2, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152416173958464","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-19 17:35:17.83', '2025-09-19 17:35:06.691', '2025-09-19 17:35:17.83');
INSERT INTO public.t_ds_process_definition_log VALUES (61, 152439162752320, '转化_水位预警等级校验', 1, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:09:32.174', '2025-09-20 00:09:32.174', '2025-09-20 00:09:32.174');
INSERT INTO public.t_ds_process_definition_log VALUES (62, 152439162752320, '转化_水位预警等级校验', 2, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:13:15.042', '2025-09-20 00:09:32.174', '2025-09-20 00:13:15.042');
INSERT INTO public.t_ds_process_definition_log VALUES (63, 152439162752320, '转化_水位预警等级校验', 3, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:21:50.382', '2025-09-20 00:09:32.174', '2025-09-20 00:21:50.382');
INSERT INTO public.t_ds_process_definition_log VALUES (64, 152439162752320, '转化_水位预警等级校验', 4, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:22:23.626', '2025-09-20 00:09:32.174', '2025-09-20 00:22:23.626');
INSERT INTO public.t_ds_process_definition_log VALUES (65, 152439162752320, '转化_水位预警等级校验', 5, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:22:31.416', '2025-09-20 00:09:32.174', '2025-09-20 00:22:31.416');
INSERT INTO public.t_ds_process_definition_log VALUES (66, 152439162752320, '转化_水位预警等级校验', 6, '【达梦 → Doris】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-20 00:23:50.718', '2025-09-20 00:09:32.174', '2025-09-20 00:23:50.718');
INSERT INTO public.t_ds_process_definition_log VALUES (67, 152642396472640, '转化_水位列转行', 1, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-22 09:25:52.357', '2025-09-22 09:25:52.357', '2025-09-22 09:25:52.357');
INSERT INTO public.t_ds_process_definition_log VALUES (68, 152642396472640, '转化_水位列转行', 2, '【达梦原始层 → Doris 明细层】将多字段水位统计表转为行式明细', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-22 09:26:30.284', '2025-09-22 09:25:52.357', '2025-09-22 09:26:30.284');
INSERT INTO public.t_ds_process_definition_log VALUES (69, 152439162752320, '转化_水位预警等级校验', 7, '【达梦 → Doris 应用层】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":260,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-22 09:26:58.954', '2025-09-20 00:09:32.174', '2025-09-22 09:26:58.954');
INSERT INTO public.t_ds_process_definition_log VALUES (70, 152642396472640, '转化_水位列转行', 3, '【达梦 → Doris 明细层】将多字段水位统计表转为行式明细', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-22 09:27:17.484', '2025-09-22 09:25:52.357', '2025-09-22 09:27:17.484');
INSERT INTO public.t_ds_process_definition_log VALUES (73, 152648927087936, '转化_水位行转列', 1, '【达梦 → Doris】将逐时水位记录转为列式存储', 152317790975712, 0, 1, '[]', '[{"taskCode":152648927077696,"x":320,"y":150}]', NULL, 1, 0, 0, 1, '2025-09-22 11:19:31.097', '2025-09-22 11:19:31.097', '2025-09-22 11:19:31.097');
INSERT INTO public.t_ds_process_definition_log VALUES (74, 152649931035968, '转化_站点表字符串拼接', 1, '【达梦 → Doris 明细层】将站点表中的字符串字段进行拼接转换', 152317790975712, 0, 1, '[]', '[{"taskCode":152649930990912,"x":360,"y":230}]', NULL, 1, 0, 0, 1, '2025-09-22 11:37:12.771', '2025-09-22 11:37:12.771', '2025-09-22 11:37:12.771');
INSERT INTO public.t_ds_process_definition_log VALUES (79, 152653935311168, '转化_站点数据去重', 1, '【达梦 → Doris 原始层】对站点水位监测站点数据进行去重处理', 152317790975712, 0, 1, '[]', '[{"taskCode":152653935271232,"x":270,"y":200}]', NULL, 1, 0, 0, 1, '2025-09-22 12:46:47.632', '2025-09-22 12:46:47.632', '2025-09-22 12:46:47.632');
INSERT INTO public.t_ds_process_definition_log VALUES (80, 152654997830976, '多表_更新写', 1, '【达梦 → Doris 主题层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152654997784896,"x":210,"y":160}]', NULL, 1, 0, 0, 1, '2025-09-22 13:05:39.183', '2025-09-22 13:05:39.183', '2025-09-22 13:05:39.183');
INSERT INTO public.t_ds_process_definition_log VALUES (81, 152648927087936, '转化_水位行转列', 2, '【达梦 → Doris 明细层】将逐时水位记录转为列式存储', 152317790975712, 0, 1, '[]', '[{"taskCode":152648927077696,"x":320,"y":150}]', NULL, 1, 0, 0, 1, '2025-09-22 13:06:01.996', '2025-09-22 11:19:31.097', '2025-09-22 13:06:01.996');
INSERT INTO public.t_ds_process_definition_log VALUES (82, 152654997830976, '多表_更新写', 2, '【达梦 → Doris 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152654997784896,"x":210,"y":160}]', NULL, 1, 0, 0, 1, '2025-09-22 13:10:04.164', '2025-09-22 13:05:39.183', '2025-09-22 13:10:04.164');
INSERT INTO public.t_ds_process_definition_log VALUES (83, 152655297809728, '多表_全量写', 1, '【达梦 → Doris 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152655297790272,"x":400,"y":130}]', NULL, 1, 0, 0, 1, '2025-09-22 13:10:50.235', '2025-09-22 13:10:50.235', '2025-09-22 13:10:50.235');
INSERT INTO public.t_ds_process_definition_log VALUES (84, 152655501885760, '多表_追加写', 1, '【达梦 → Doris 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152655501865280,"x":250,"y":190}]', NULL, 1, 0, 0, 1, '2025-09-22 13:14:24.026', '2025-09-22 13:14:24.026', '2025-09-22 13:14:24.026');
INSERT INTO public.t_ds_process_definition_log VALUES (85, 152656044221760, '单表_全量读_全量写', 1, '【达梦 → Doris 原始层】全量读取水位监测表，日全量重建', 152317790975712, 0, 1, '[]', '[{"taskCode":152656044210496,"x":260,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-22 13:23:43.684', '2025-09-22 13:23:43.684', '2025-09-22 13:23:43.684');
INSERT INTO public.t_ds_process_definition_log VALUES (86, 152656278456640, '单表_时间增量读_更新写', 1, '【达梦 → Doris 原始层】按时间增量采集水位数据，实时更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152656278439232,"x":320,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-22 13:27:45.333', '2025-09-22 13:27:45.333', '2025-09-22 13:27:45.333');
INSERT INTO public.t_ds_process_definition_log VALUES (87, 152656664685888, '单表_ID增量读_追加写', 1, '【达梦 → Doris 原始层】按 ID 增量采集水位数据，定时追加', 152317790975712, 0, 1, '[]', '[{"taskCode":152656664673600,"x":340,"y":160}]', NULL, 1, 0, 0, 1, '2025-09-22 13:34:27.701', '2025-09-22 13:34:27.701', '2025-09-22 13:34:27.701');
INSERT INTO public.t_ds_process_definition_log VALUES (88, 152657219441984, '清洗_水位格式标准化', 1, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152657219431744,"x":280,"y":180}]', NULL, 1, 0, 0, 1, '2025-09-22 13:44:11.848', '2025-09-22 13:44:11.848', '2025-09-22 13:44:11.848');
INSERT INTO public.t_ds_process_definition_log VALUES (89, 152657219441984, '清洗_水位格式标准化', 2, '【达梦 → Doris 明细层】统一水位监测数据格式', 152317790975712, 0, 1, '[]', '[{"taskCode":152657219431744,"x":280,"y":180}]', NULL, 1, 0, 0, 1, '2025-09-22 13:44:26.201', '2025-09-22 13:44:11.848', '2025-09-22 13:44:26.201');
INSERT INTO public.t_ds_process_definition_log VALUES (92, 152657963472192, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152657963461952","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-22 13:57:17.76', '2025-09-22 13:57:17.76', '2025-09-22 13:57:17.76');
INSERT INTO public.t_ds_process_definition_log VALUES (93, 152658250428736, '清洗_水位缺失补全', 1, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152658250408256,"x":300,"y":60}]', NULL, 1, 0, 0, 1, '2025-09-22 14:02:24.009', '2025-09-22 14:02:24.009', '2025-09-22 14:02:24.009');
INSERT INTO public.t_ds_process_definition_log VALUES (94, 152658250428736, '清洗_水位缺失补全', 2, '【达梦 → Doris 明细层】补全水位监测数据缺失值', 152317790975712, 0, 1, '[]', '[{"taskCode":152658250408256,"x":300,"y":60}]', NULL, 1, 0, 0, 1, '2025-09-22 14:02:36.74', '2025-09-22 14:02:24.009', '2025-09-22 14:02:36.74');
INSERT INTO public.t_ds_process_definition_log VALUES (95, 152658885689664, '清洗_水位异常值处理', 1, '【达梦 → Doris 明细层】清洗水位监测数据，剔除异常值', 152317790975712, 0, 1, '[]', '[{"taskCode":152658885655872,"x":350,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-22 14:13:36.295', '2025-09-22 14:13:36.295', '2025-09-22 14:13:36.295');
INSERT INTO public.t_ds_process_definition_log VALUES (104, 152416165530944, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 3, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152662937492800","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-22 15:24:10.635', '2025-09-19 17:35:06.691', '2025-09-22 15:24:10.635');
INSERT INTO public.t_ds_process_definition_log VALUES (119, 152665378283840, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"152665378277696","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-22 16:07:25.877', '2025-09-22 16:07:25.877', '2025-09-22 16:07:25.877');
INSERT INTO public.t_ds_process_definition_log VALUES (145, 152672440882496, 'Hive_水位日统计报表', 1, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 0, 1, '[]', '[{"taskCode":152672440882496,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-22 18:03:55.894', '2025-09-22 18:03:55.894', '2025-09-22 18:03:55.894');
INSERT INTO public.t_ds_process_definition_log VALUES (146, 152672440882496, 'Hive_水位日统计报表', 2, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 0, 1, '[]', '[{"taskCode":152672440882496,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-22 18:06:41.802', '2025-09-22 18:03:55.894', '2025-09-22 18:06:41.802');
INSERT INTO public.t_ds_process_definition_log VALUES (149, 152672766952768, 'MySQL_水位日统计', 1, '【MySQL  存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 0, 1, '[]', '[{"taskCode":152672766952768,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-22 18:13:05.613', '2025-09-22 18:13:05.613', '2025-09-22 18:13:05.613');
INSERT INTO public.t_ds_process_definition_log VALUES (150, 152673403135296, '作业_水位预警计算_串行', 1, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152673322082624,"x":330,"y":120},{"taskCode":152673327374656,"x":330,"y":220},{"taskCode":152673332505920,"x":330,"y":320}]', NULL, 1, 0, 0, 1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:24.225', '2025-09-22 18:19:24.225');
INSERT INTO public.t_ds_process_definition_log VALUES (151, 152673403135296, '作业_水位预警计算_串行', 2, '【依次执行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673322082624,"x":330,"y":120},{"taskCode":152673327374656,"x":330,"y":220},{"taskCode":152673332505920,"x":330,"y":320}]', NULL, 1, 0, 0, 1, '2025-09-22 18:19:42.908', '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.908');
INSERT INTO public.t_ds_process_definition_log VALUES (152, 152673582845248, '作业_水位小时均值_并行', 1, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":300,"y":310},{"taskCode":152673488823616,"x":80,"y":-20},{"taskCode":152673498672448,"x":80,"y":110},{"taskCode":152673502172480,"x":500,"y":110},{"taskCode":152673511663936,"x":500,"y":-20}]', NULL, 1, 0, 0, 1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_process_definition_log VALUES (153, 152673582845248, '作业_水位小时均值_并行', 2, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":300,"y":310},{"taskCode":152673488823616,"x":80,"y":-20},{"taskCode":152673498672448,"x":80,"y":110},{"taskCode":152673502172480,"x":500,"y":110},{"taskCode":152673511663936,"x":500,"y":-20}]', NULL, 1, 0, 0, 1, '2025-09-22 18:23:31.275', '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.275');
INSERT INTO public.t_ds_process_definition_log VALUES (154, 152673582845248, '作业_水位小时均值_并行', 3, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":410,"y":750},{"taskCode":152673488823616,"x":190,"y":420},{"taskCode":152673498672448,"x":190,"y":550},{"taskCode":152673502172480,"x":610,"y":550},{"taskCode":152673511663936,"x":610,"y":420}]', NULL, 1, 0, 0, 1, '2025-09-22 18:23:38.454', '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.454');
INSERT INTO public.t_ds_process_definition_log VALUES (155, 152673582845248, '作业_水位小时均值_并行', 4, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":440,"y":670},{"taskCode":152673488823616,"x":220,"y":340},{"taskCode":152673498672448,"x":220,"y":470},{"taskCode":152673502172480,"x":640,"y":470},{"taskCode":152673511663936,"x":640,"y":340}]', NULL, 1, 0, 0, 1, '2025-09-22 18:23:46.571', '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.571');
INSERT INTO public.t_ds_process_definition_log VALUES (156, 152673582845248, '作业_水位小时均值_并行', 5, '【并行】', 152317790975712, 0, 1, '[]', '[{"taskCode":152673475740992,"x":390,"y":450},{"taskCode":152673488823616,"x":170,"y":120},{"taskCode":152673498672448,"x":170,"y":250},{"taskCode":152673502172480,"x":590,"y":250},{"taskCode":152673511663936,"x":590,"y":120}]', NULL, 1, 0, 0, 1, '2025-09-22 18:23:52.585', '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.585');
INSERT INTO public.t_ds_process_definition_log VALUES (163, 152439162752320, '转化_水位预警等级校验', 8, '【达梦 → Doris 应用层】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":280,"y":70}]', NULL, 1, 0, 0, 1, '2025-09-30 14:34:20.043', '2025-09-20 00:09:32.174', '2025-09-30 14:34:20.043');
INSERT INTO public.t_ds_process_definition_log VALUES (164, 153379497094688, '站点编码非空与格式稽查sjuNHe11759215156767', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"153379496920608","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-30 14:52:36.949', '2025-09-30 14:52:36.949', '2025-09-30 14:52:36.949');
INSERT INTO public.t_ds_process_definition_log VALUES (165, 153379745251872, '水位重复记录稽查M26OVzY1759215413354', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"153379745225248","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-30 14:56:53.386', '2025-09-30 14:56:53.386', '2025-09-30 14:56:53.386');
INSERT INTO public.t_ds_process_definition_log VALUES (166, 152439162752320, '转化_水位预警等级校验', 9, '【达梦 → Doris 应用层】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":280,"y":70}]', NULL, 1, 0, 0, 1, '2025-09-30 15:20:03.004', '2025-09-20 00:09:32.174', '2025-09-30 15:20:03.004');
INSERT INTO public.t_ds_process_definition_log VALUES (167, 152439162752320, '转化_水位预警等级校验', 10, '【达梦 → 应用层】据水位阈值划分红/黄/蓝三级预警', 152317790975712, 0, 1, '[]', '[{"taskCode":152439162287424,"x":280,"y":70}]', NULL, 1, 0, 0, 1, '2025-09-30 15:22:37.588', '2025-09-20 00:09:32.174', '2025-09-30 15:22:37.588');
INSERT INTO public.t_ds_process_definition_log VALUES (168, 152642396472640, '转化_水位列转行', 4, '【达梦 → 明细层】将多字段水位统计表转为行式明细', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-30 15:26:33.751', '2025-09-22 09:25:52.357', '2025-09-30 15:26:33.751');
INSERT INTO public.t_ds_process_definition_log VALUES (169, 153381689458208, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 1, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"153381689437728","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-30 15:30:35.551', '2025-09-30 15:30:35.551', '2025-09-30 15:30:35.551');
INSERT INTO public.t_ds_process_definition_log VALUES (170, 152642396472640, '转化_水位列转行', 5, '【达梦 → 明细层】将多字段水位统计表转为行式明细', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-30 15:32:21.236', '2025-09-22 09:25:52.357', '2025-09-30 15:32:21.236');
INSERT INTO public.t_ds_process_definition_log VALUES (171, 153379745251872, '水位重复记录稽查VsFeLJH1759217760217', 2, '', 147372832245312, 0, 1, '[]', '[{"taskCode":"153381999104544","x":138.4886474609375,"y":184.9232940673828}]', NULL, 1, 0, 0, 1, '2025-09-30 15:36:00.255', '2025-09-30 14:56:53.386', '2025-09-30 15:36:00.255');
INSERT INTO public.t_ds_process_definition_log VALUES (172, 152642396472640, '转化_水位列转行', 6, '【达梦 → 明细层】将多字段水位统计表转为行式明细', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-30 15:36:29.857', '2025-09-22 09:25:52.357', '2025-09-30 15:36:29.857');
INSERT INTO public.t_ds_process_definition_log VALUES (173, 152642396472640, '转化_排序记录', 7, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-30 15:38:05.937', '2025-09-22 09:25:52.357', '2025-09-30 15:38:05.937');
INSERT INTO public.t_ds_process_definition_log VALUES (174, 152642396472640, '转化_水位列转行', 8, '【达梦 →  明细层】将多字段水位统计表排序输出', 152317790975712, 0, 1, '[]', '[{"taskCode":152642396352832,"x":370,"y":90}]', NULL, 1, 0, 0, 1, '2025-09-30 15:38:29.549', '2025-09-22 09:25:52.357', '2025-09-30 15:38:29.549');
INSERT INTO public.t_ds_process_definition_log VALUES (176, 152649931035968, '转化_站点表字符串拼接', 2, '【达梦 → 明细层】将站点表中的字符串字段进行拼接转换', 152317790975712, 0, 1, '[]', '[{"taskCode":152649930990912,"x":360,"y":230}]', NULL, 1, 0, 0, 1, '2025-09-30 15:41:18.338', '2025-09-22 11:37:12.771', '2025-09-30 15:41:18.338');
INSERT INTO public.t_ds_process_definition_log VALUES (179, 152654997830976, '多表_更新写', 3, '【达梦 →  主题层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152654997784896,"x":210,"y":160}]', NULL, 1, 0, 0, 1, '2025-09-30 15:45:32.519', '2025-09-22 13:05:39.183', '2025-09-30 15:45:32.519');
INSERT INTO public.t_ds_process_definition_log VALUES (180, 152655297809728, '多表_全量写', 2, '【达梦 → 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152655297790272,"x":400,"y":130}]', NULL, 1, 0, 0, 1, '2025-09-30 15:47:08.56', '2025-09-22 13:10:50.235', '2025-09-30 15:47:08.56');
INSERT INTO public.t_ds_process_definition_log VALUES (181, 152655297809728, '多表_全量写', 3, '【达梦 → 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152655297790272,"x":400,"y":130}]', NULL, 1, 0, 0, 1, '2025-09-30 15:47:19.518', '2025-09-22 13:10:50.235', '2025-09-30 15:47:19.518');
INSERT INTO public.t_ds_process_definition_log VALUES (182, 152655501885760, '多表_追加写', 2, '【达梦 → 应用层】结合测站信息和测站月统计报表统计流域月报表，按天更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152655501865280,"x":250,"y":190}]', NULL, 1, 0, 0, 1, '2025-09-30 15:48:35.666', '2025-09-22 13:14:24.026', '2025-09-30 15:48:35.666');
INSERT INTO public.t_ds_process_definition_log VALUES (186, 152656278456640, '单表_时间增量读_更新写', 2, '【达梦 → Doris 原始层】按时间增量采集水位数据，实时更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152656278439232,"x":320,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-30 15:55:28.764', '2025-09-22 13:27:45.333', '2025-09-30 15:55:28.764');
INSERT INTO public.t_ds_process_definition_log VALUES (188, 152656664685888, '单表_ID增量读_追加写', 2, '【达梦 → 原始层】按 ID 增量采集水位数据，定时追加', 152317790975712, 0, 1, '[]', '[{"taskCode":152656664673600,"x":340,"y":160}]', NULL, 1, 0, 0, 1, '2025-09-30 15:57:29.434', '2025-09-22 13:34:27.701', '2025-09-30 15:57:29.434');
INSERT INTO public.t_ds_process_definition_log VALUES (175, 152648927087936, '转化_水位行转列', 3, '【达梦 → 明细层】将逐时水位记录转为列式存储', 152317790975712, 0, 1, '[]', '[{"taskCode":152648927077696,"x":320,"y":150}]', NULL, 1, 0, 0, 1, '2025-09-30 15:39:23.613', '2025-09-22 11:19:31.097', '2025-09-30 15:39:23.613');
INSERT INTO public.t_ds_process_definition_log VALUES (178, 152653935311168, '转化_站点数据去重', 3, '【达梦 →  原始层】对站点水位监测站点数据进行去重处理', 152317790975712, 0, 1, '[]', '[{"taskCode":152653935271232,"x":270,"y":150}]', NULL, 1, 0, 0, 1, '2025-09-30 15:44:07.47', '2025-09-22 12:46:47.632', '2025-09-30 15:44:07.47');
INSERT INTO public.t_ds_process_definition_log VALUES (187, 152656278456640, '单表_时间增量读_更新写', 3, '【达梦 →  原始层】按时间增量采集水位数据，实时更新', 152317790975712, 0, 1, '[]', '[{"taskCode":152656278439232,"x":320,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-30 15:56:31.3', '2025-09-22 13:27:45.333', '2025-09-30 15:56:31.3');
INSERT INTO public.t_ds_process_definition_log VALUES (177, 152653935311168, '转化_站点数据去重', 2, '【达梦 → Doris 原始层】对站点水位监测站点数据进行去重处理', 152317790975712, 0, 1, '[]', '[{"taskCode":152653935271232,"x":270,"y":150}]', NULL, 1, 0, 0, 1, '2025-09-30 15:43:52.617', '2025-09-22 12:46:47.632', '2025-09-30 15:43:52.617');
INSERT INTO public.t_ds_process_definition_log VALUES (184, 152656044221760, '单表_全量读_全量写', 3, '【达梦 → 原始层】全量读取水位监测表，日全量重建', 152317790975712, 0, 1, '[]', '[{"taskCode":152656044210496,"x":260,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-30 15:50:38.666', '2025-09-22 13:23:43.684', '2025-09-30 15:50:38.666');
INSERT INTO public.t_ds_process_definition_log VALUES (185, 152656044221760, '单表_全量读_全量写', 4, '【达梦 → 原始层】全量读取水位监测表，日全量重建', 152317790975712, 0, 1, '[]', '[{"taskCode":152656044210496,"x":260,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-30 15:50:50.097', '2025-09-22 13:23:43.684', '2025-09-30 15:50:50.097');
INSERT INTO public.t_ds_process_definition_log VALUES (183, 152656044221760, '单表_全量读_全量写', 2, '【达梦 → Doris 原始层】全量读取水位监测表，日全量重建', 152317790975712, 0, 1, '[]', '[{"taskCode":152656044210496,"x":260,"y":170}]', NULL, 1, 0, 0, 1, '2025-09-30 15:50:31.858', '2025-09-22 13:23:43.684', '2025-09-30 15:50:31.858');
INSERT INTO public.t_ds_process_definition_log VALUES (189, 152657219441984, '清洗_水位值边界调整', 3, '【达梦 → 明细层】水位值边界调整', 152317790975712, 0, 1, '[]', '[{"taskCode":152657219431744,"x":280,"y":180}]', NULL, 1, 0, 0, 1, '2025-09-30 16:00:15.169', '2025-09-22 13:44:11.848', '2025-09-30 16:00:15.169');
INSERT INTO public.t_ds_process_definition_log VALUES (190, 152658250428736, '清洗_水位添加单位', 3, '', 152317790975712, 0, 1, '[]', '[{"taskCode":152658250408256,"x":300,"y":60}]', NULL, 1, 0, 0, 1, '2025-09-30 16:03:07.118', '2025-09-22 14:02:24.009', '2025-09-30 16:03:07.118');
INSERT INTO public.t_ds_process_definition_log VALUES (191, 152658250428736, '清洗_水位缺失补全', 4, '【达梦 →明细层】补全水位单位', 152317790975712, 0, 1, '[]', '[{"taskCode":152658250408256,"x":300,"y":60}]', NULL, 1, 0, 0, 1, '2025-09-30 16:03:29.977', '2025-09-22 14:02:24.009', '2025-09-30 16:03:29.977');
INSERT INTO public.t_ds_process_definition_log VALUES (192, 152658885689664, '清洗_水位异常值处理', 2, '【达梦 → 明细层】清洗水位监测数据，剔除异常值', 152317790975712, 0, 1, '[]', '[{"taskCode":152658885655872,"x":350,"y":140}]', NULL, 1, 0, 0, 1, '2025-09-30 16:05:39.369', '2025-09-22 14:13:36.295', '2025-09-30 16:05:39.369');
INSERT INTO public.t_ds_process_definition_log VALUES (193, 153383838261792, 'Kingbase_水位日统计', 1, '', 152317790975712, 0, 1, '[]', '[{"taskCode":153383838261792,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-30 16:08:34.03', '2025-09-30 16:08:34.03', '2025-09-30 16:08:34.03');
INSERT INTO public.t_ds_process_definition_log VALUES (194, 153383838261792, 'Kingbase_水位日统计', 2, '【Kingbase 存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 0, 1, '[]', '[{"taskCode":153383838261792,"x":0,"y":0}]', NULL, 1, 0, 0, 1, '2025-09-30 16:09:14.437', '2025-09-30 16:08:34.03', '2025-09-30 16:09:14.437');


--
-- Data for Name: t_ds_process_instance; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_process_instance VALUES (5, '测试9LMXA6X1756874378318-2-20250903123942541', 150982603138816, 2, 147372832245312, 7, '[{"time":"2025-09-03 12:39:42","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-03 12:39:42","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-03 12:39:43","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-03 12:39:42.544', '2025-09-03 12:39:43.808', 1, '172.26.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-03 12:39:42.115', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"测试9LMXA6X1756874378318.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-03 12:39:42.546', 0);
INSERT INTO public.t_ds_process_instance VALUES (21, '工程监管数据发现uVOWKbY1758271591783-1-20250919164634748', 152413381367104, 1, 134799536571008, 7, '[{"time":"2025-09-19 16:46:34","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 16:46:34","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 16:46:38","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 16:46:34.756', '2025-09-19 16:46:38.075', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 16:46:33.706', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"工程监管数据发现uVOWKbY1758271591783.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 16:46:34.781', 0);
INSERT INTO public.t_ds_process_instance VALUES (14, '232332-1-20250916172439585', 152150248157120, 1, 141883958809440, 7, '[{"time":"2025-09-16 17:24:39","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:24:39","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:24:53","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:24:39.592', '2025-09-16 17:24:53.192', 1, '172.24.0.15:5678', 0, '{}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:24:39.368', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[]', 0, 0, '2025-09-16 17:24:39.596', 0);
INSERT INTO public.t_ds_process_instance VALUES (18, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916172954229', 152150622981056, 1, 147372832245312, 7, '[{"time":"2025-09-16 17:29:54","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:29:54","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:29:56","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:29:54.229', '2025-09-16 17:29:56.23', 1, '172.24.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:29:54.064', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-16 17:29:54.23', 0);
INSERT INTO public.t_ds_process_instance VALUES (15, '232332-1-20250916172528723', 152150248157120, 1, 141883958809440, 7, '[{"time":"2025-09-16 17:25:28","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:25:28","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:25:40","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:25:28.723', '2025-09-16 17:25:40.176', 1, '172.24.0.15:5678', 0, '{}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:25:28.179', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[]', 0, 0, '2025-09-16 17:25:28.723', 0);
INSERT INTO public.t_ds_process_instance VALUES (16, '232332-1-20250916172703895', 152150248157120, 1, 141883958809440, 7, '[{"time":"2025-09-16 17:27:03","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:27:03","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:27:16","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:27:03.896', '2025-09-16 17:27:16.208', 1, '172.24.0.15:5678', 0, '{}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:27:03.174', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[]', 0, 0, '2025-09-16 17:27:03.898', 0);
INSERT INTO public.t_ds_process_instance VALUES (20, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916173004269', 152150622981056, 1, 147372832245312, 7, '[{"time":"2025-09-16 17:30:04","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:30:04","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:30:06","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:30:04.27', '2025-09-16 17:30:06.246', 1, '172.24.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:30:03.726', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-16 17:30:04.27', 0);
INSERT INTO public.t_ds_process_instance VALUES (17, '232332Cjy7WIv1758014906806-2-20250916172831065', 152150518585280, 2, 134799536571008, 7, '[{"time":"2025-09-16 17:28:31","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:28:31","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:28:33","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:28:31.065', '2025-09-16 17:28:33.219', 1, '172.24.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:28:30.841', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"232332Cjy7WIv1758014906806.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-16 17:28:31.066', 0);
INSERT INTO public.t_ds_process_instance VALUES (19, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916172957244', 152150622981056, 1, 147372832245312, 7, '[{"time":"2025-09-16 17:29:57","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-16 17:29:57","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-16 17:29:59","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-16 17:29:57.245', '2025-09-16 17:29:59.248', 1, '172.24.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-16 17:29:56.485', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-16 17:29:57.245', 0);
INSERT INTO public.t_ds_process_instance VALUES (22, '工程监管数据发现wSAxLkg1758271769213-2-20250919164937792', 152413381367104, 2, 134799536571008, 7, '[{"time":"2025-09-19 16:49:37","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 16:49:37","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 16:49:39","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 16:49:37.792', '2025-09-19 16:49:39.521', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 16:49:36.829', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"工程监管数据发现wSAxLkg1758271769213.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 16:49:37.792', 0);
INSERT INTO public.t_ds_process_instance VALUES (23, '水文监测数据发现Rg0XxWU1758271770687-1-20250919164939811', 152413555449152, 1, 134799536571008, 7, '[{"time":"2025-09-19 16:49:39","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 16:49:39","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 16:49:42","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 16:49:39.811', '2025-09-19 16:49:42.421', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 16:49:39.76', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水文监测数据发现Rg0XxWU1758271770687.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 16:49:39.812', 0);
INSERT INTO public.t_ds_process_instance VALUES (24, '水质监测数据发现OemKK9R1758271772314-1-20250919164942828', 152413557117248, 1, 134799536571008, 7, '[{"time":"2025-09-19 16:49:42","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 16:49:42","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 16:49:45","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 16:49:42.828', '2025-09-19 16:49:45.424', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 16:49:42.191', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水质监测数据发现OemKK9R1758271772314.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 16:49:42.829', 0);
INSERT INTO public.t_ds_process_instance VALUES (25, '水资源数据发现xAxpna41758271796919-1-20250919164958873', 152413579812160, 1, 134799536571008, 7, '[{"time":"2025-09-19 16:49:58","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 16:49:58","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 16:50:00","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 16:49:58.873', '2025-09-19 16:50:00.466', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 16:49:58.458', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水资源数据发现xAxpna41758271796919.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 16:49:58.873', 0);
INSERT INTO public.t_ds_process_instance VALUES (144, '水位重复记录稽查M26OVzY1759215413354-1-20250930145657939', 153379745251872, 1, 147372832245312, 7, '[{"time":"2025-09-30 14:56:57","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 14:56:57","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 14:57:01","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 14:56:57.939', '2025-09-30 14:57:01.611', 1, '172.28.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-30 14:56:57.327', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水位重复记录稽查M26OVzY1759215413354.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-30 14:56:57.94', 0);
INSERT INTO public.t_ds_process_instance VALUES (29, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810-2-20250919173520670', 152416165530944, 2, 147372832245312, 7, '[{"time":"2025-09-19 17:35:20","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 17:35:20","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 17:35:22","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 17:35:20.67', '2025-09-19 17:35:22.242', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 17:35:20.256', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 17:35:20.671', 0);
INSERT INTO public.t_ds_process_instance VALUES (28, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810-2-20250919173519630', 152416165530944, 2, 147372832245312, 7, '[{"time":"2025-09-19 17:35:19","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-19 17:35:19","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-19 17:35:21","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-19 17:35:19.631', '2025-09-19 17:35:21.24', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-19 17:35:18.827', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-19 17:35:19.633', 0);
INSERT INTO public.t_ds_process_instance VALUES (70, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618-3-20250922152413821', 152416165530944, 3, 147372832245312, 7, '[{"time":"2025-09-22 15:24:13","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-22 15:24:13","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-22 15:24:15","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-22 15:24:13.822', '2025-09-22 15:24:15.852', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-22 15:24:13.183', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-22 15:24:13.824', 0);
INSERT INTO public.t_ds_process_instance VALUES (82, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250922160728245', 152665378283840, 1, 147372832245312, 7, '[{"time":"2025-09-22 16:07:28","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-22 16:07:28","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-22 16:07:29","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-22 16:07:28.245', '2025-09-22 16:07:29.842', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-22 16:07:28.089', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-22 16:07:28.246', 0);
INSERT INTO public.t_ds_process_instance VALUES (164, '清洗_水位异常值处理-2-20250930160545028', 152658885689664, 2, 152317790975712, 7, '[{"time":"2025-09-30 16:05:45","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 16:05:45","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 16:06:27","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 16:05:45.028', '2025-09-30 16:06:27.15', 1, '172.28.0.15:5678', 0, '{}', 2, 0, 1, 0, 0, NULL, '2025-09-30 16:05:44.509', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[]', 0, 0, '2025-09-30 16:05:45.029', 0);
INSERT INTO public.t_ds_process_instance VALUES (165, 'MySQL_水位日统计-1-20250930160854082', 152672766952768, 1, 152317790975712, 6, '[{"time":"2025-09-30 16:08:54","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 16:08:54","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 16:08:57","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-30 16:08:54.082', '2025-09-30 16:08:57.896', 1, '172.28.0.15:5678', 0, '{}', 2, 0, 1, 0, 0, NULL, '2025-09-30 16:08:53.719', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-30 16:08:54.083', 0);
INSERT INTO public.t_ds_process_instance VALUES (102, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250923000001506', 152665378283840, 1, 147372832245312, 7, '[{"time":"2025-09-23 00:00:01","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-23 00:00:01","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-23 00:00:03","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-23 00:00:01.513', '2025-09-23 00:00:03.176', 1, '172.28.0.11:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-23 00:00:00', '2025-09-23 00:00:00.557', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-23 00:00:01.522', 0);
INSERT INTO public.t_ds_process_instance VALUES (147, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526-1-20250930153037751', 153381689458208, 1, 147372832245312, 7, '[{"time":"2025-09-30 15:30:37","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 15:30:37","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 15:30:39","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 15:30:37.752', '2025-09-30 15:30:39.278', 1, '172.28.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-30 15:30:37.296', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-30 15:30:37.752', 0);
INSERT INTO public.t_ds_process_instance VALUES (55, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745-1-20250922135719280', 152657963472192, 1, 147372832245312, 7, '[{"time":"2025-09-22 13:57:19","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-22 13:57:19","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-22 13:57:21","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-22 13:57:19.281', '2025-09-22 13:57:21.986', 1, '172.28.0.11:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-22 13:57:18.694', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-22 13:57:19.282', 0);
INSERT INTO public.t_ds_process_instance VALUES (148, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526-1-20250930153040784', 153381689458208, 1, 147372832245312, 7, '[{"time":"2025-09-30 15:30:40","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 15:30:40","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 15:30:42","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 15:30:40.784', '2025-09-30 15:30:42.274', 1, '172.28.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-30 15:30:39.828', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-30 15:30:40.785', 0);
INSERT INTO public.t_ds_process_instance VALUES (151, '水位重复记录稽查VsFeLJH1759217760217-2-20250930153605909', 153379745251872, 2, 147372832245312, 7, '[{"time":"2025-09-30 15:36:05","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 15:36:05","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 15:36:07","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 15:36:05.909', '2025-09-30 15:36:07.92', 1, '172.28.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-30 15:36:05.179', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"水位重复记录稽查VsFeLJH1759217760217.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-30 15:36:05.912', 0);
INSERT INTO public.t_ds_process_instance VALUES (130, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-26 13:56:07","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-26 13:55:56.243', '2025-09-26 13:56:07.246', 1, '172.18.0.15:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-24 00:00:00', '2025-09-26 13:55:48.342', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-26 13:55:56.257', 0);
INSERT INTO public.t_ds_process_instance VALUES (132, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-26 13:56:07","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-26 13:55:56.241', '2025-09-26 13:56:07.246', 1, '172.18.0.15:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-26 00:00:00', '2025-09-26 13:55:48.417', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-26 13:55:56.257', 0);
INSERT INTO public.t_ds_process_instance VALUES (131, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-26 13:55:56","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-26 13:56:07","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-26 13:55:56.241', '2025-09-26 13:56:07.246', 1, '172.18.0.15:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-25 00:00:00', '2025-09-26 13:55:48.398', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-26 13:55:56.257', 0);
INSERT INTO public.t_ds_process_instance VALUES (133, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250928110617544', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-28 11:06:17","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-28 11:06:17","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-28 11:06:19","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-28 11:06:17.549', '2025-09-28 11:06:19.744', 1, '172.28.0.14:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-27 00:00:00', '2025-09-28 11:06:09.584', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-28 11:06:17.568', 0);
INSERT INTO public.t_ds_process_instance VALUES (134, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250928110617544', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-28 11:06:17","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-28 11:06:17","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-28 11:06:19","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-28 11:06:17.548', '2025-09-28 11:06:19.745', 1, '172.28.0.14:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-28 00:00:00', '2025-09-28 11:06:09.614', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-28 11:06:17.568', 0);
INSERT INTO public.t_ds_process_instance VALUES (136, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250930093424358', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-30 09:34:24","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 09:34:24","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-30 09:34:26","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-30 09:34:24.364', '2025-09-30 09:34:26.545', 1, '172.28.0.15:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-30 00:00:00', '2025-09-30 09:34:16.417', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-30 09:34:24.381', 0);
INSERT INTO public.t_ds_process_instance VALUES (135, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250930093424358', 152665378283840, 1, 147372832245312, 6, '[{"time":"2025-09-30 09:34:24","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 09:34:24","state":"RUNNING_EXECUTION","desc":"start a new process from scheduler"},{"time":"2025-09-30 09:34:26","state":"FAILURE","desc":"update by workflow executor"}]', 0, '2025-09-30 09:34:24.362', '2025-09-30 09:34:26.545', 1, '172.28.0.15:5678', 6, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 1, '2025-09-29 00:00:00', '2025-09-30 09:34:16.391', NULL, NULL, 1, NULL, 0, 1, 'admin', 'SCHEDULER', NULL, 2, 'default', -1, 0, 'default', NULL, 0, 0, '2025-09-30 09:34:24.381', 0);
INSERT INTO public.t_ds_process_instance VALUES (142, '站点编码非空与格式稽查sjuNHe11759215156767-1-20250930145239258', 153379497094688, 1, 147372832245312, 7, '[{"time":"2025-09-30 14:52:39","state":"RUNNING_EXECUTION","desc":"init running"},{"time":"2025-09-30 14:52:39","state":"RUNNING_EXECUTION","desc":"start a new process"},{"time":"2025-09-30 14:52:41","state":"SUCCESS","desc":"update by workflow executor"}]', 0, '2025-09-30 14:52:39.26', '2025-09-30 14:52:41.442', 1, '172.28.0.15:5678', 0, '{"schedule_timezone":"Asia/Shanghai"}', 2, 0, 1, 0, 0, NULL, '2025-09-30 14:52:38.919', NULL, NULL, 1, NULL, 0, 1, 'admin', 'START_PROCESS', NULL, 2, 'default', -1, 0, 'default', '[{"prop":"站点编码非空与格式稽查sjuNHe11759215156767.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, 0, '2025-09-30 14:52:39.263', 0);


--
-- Data for Name: t_ds_process_task_relation; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_process_task_relation VALUES (3, NULL, 141883958809440, 150911519707360, 2, 0, 0, 150911519707360, 2, 0, NULL, '2025-09-02 17:31:15.673', '2025-09-02 17:31:15.675');
INSERT INTO public.t_ds_process_task_relation VALUES (6, NULL, 147372832245312, 150982603138816, 2, 0, 0, 150982633305856, 1, 0, NULL, '2025-09-03 12:39:38.381', '2025-09-03 12:39:38.389');
INSERT INTO public.t_ds_process_task_relation VALUES (46, NULL, 141883958809440, 152150248157120, 1, 0, 0, 152150248157120, 1, 0, NULL, '2025-09-16 17:24:34.356', '2025-09-16 17:24:34.357');
INSERT INTO public.t_ds_process_task_relation VALUES (48, NULL, 134799536571008, 152150518585280, 2, 0, 0, 152150534972352, 1, 0, NULL, '2025-09-16 17:28:26.852', '2025-09-16 17:28:26.855');
INSERT INTO public.t_ds_process_task_relation VALUES (49, NULL, 147372832245312, 152150622981056, 1, 0, 0, 152150622975936, 1, 0, NULL, '2025-09-16 17:29:52.759', '2025-09-16 17:29:52.76');
INSERT INTO public.t_ds_process_task_relation VALUES (51, NULL, 141883958809440, 152150699767744, 2, 0, 0, 152150699760576, 2, 0, NULL, '2025-09-16 17:31:49.226', '2025-09-16 17:31:49.226');
INSERT INTO public.t_ds_process_task_relation VALUES (53, NULL, 134799536571008, 152413381367104, 2, 0, 0, 152413553931584, 1, 0, NULL, '2025-09-19 16:49:29.259', '2025-09-19 16:49:29.262');
INSERT INTO public.t_ds_process_task_relation VALUES (54, NULL, 134799536571008, 152413555449152, 1, 0, 0, 152413555441984, 1, 0, NULL, '2025-09-19 16:49:30.703', '2025-09-19 16:49:30.703');
INSERT INTO public.t_ds_process_task_relation VALUES (55, NULL, 134799536571008, 152413557117248, 1, 0, 0, 152413557107008, 1, 0, NULL, '2025-09-19 16:49:32.334', '2025-09-19 16:49:32.334');
INSERT INTO public.t_ds_process_task_relation VALUES (56, NULL, 134799536571008, 152413579812160, 1, 0, 0, 152413579804992, 1, 0, NULL, '2025-09-19 16:49:56.934', '2025-09-19 16:49:56.934');
INSERT INTO public.t_ds_process_task_relation VALUES (146, NULL, 152317790975712, 152672440882496, 2, 0, 0, 152672440882496, 2, 0, NULL, '2025-09-22 18:06:41.808', '2025-09-22 18:06:41.809');
INSERT INTO public.t_ds_process_task_relation VALUES (149, NULL, 152317790975712, 152672766952768, 1, 0, 0, 152672766952768, 1, 0, NULL, '2025-09-22 18:13:05.622', '2025-09-22 18:13:05.623');
INSERT INTO public.t_ds_process_task_relation VALUES (153, NULL, 152317790975712, 152673403135296, 2, 0, 0, 152673322082624, 2, 0, NULL, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation VALUES (154, NULL, 152317790975712, 152673403135296, 2, 152673322082624, 2, 152673327374656, 2, 0, NULL, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation VALUES (155, NULL, 152317790975712, 152673403135296, 2, 152673327374656, 2, 152673332505920, 2, 0, NULL, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation VALUES (92, NULL, 147372832245312, 152657963472192, 1, 0, 0, 152657963461952, 1, 0, NULL, '2025-09-22 13:57:17.776', '2025-09-22 13:57:17.777');
INSERT INTO public.t_ds_process_task_relation VALUES (104, NULL, 147372832245312, 152416165530944, 3, 0, 0, 152662937492800, 1, 0, NULL, '2025-09-22 15:24:10.641', '2025-09-22 15:24:10.642');
INSERT INTO public.t_ds_process_task_relation VALUES (119, NULL, 147372832245312, 152665378283840, 1, 0, 0, 152665378277696, 1, 0, NULL, '2025-09-22 16:07:25.883', '2025-09-22 16:07:25.883');
INSERT INTO public.t_ds_process_task_relation VALUES (180, NULL, 152317790975712, 152673582845248, 5, 0, 0, 152673488823616, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (181, NULL, 152317790975712, 152673582845248, 5, 0, 0, 152673511663936, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (182, NULL, 152317790975712, 152673582845248, 5, 152673488823616, 5, 152673498672448, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (183, NULL, 152317790975712, 152673582845248, 5, 152673511663936, 5, 152673502172480, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (184, NULL, 152317790975712, 152673582845248, 5, 152673498672448, 5, 152673475740992, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (185, NULL, 152317790975712, 152673582845248, 5, 152673502172480, 5, 152673475740992, 5, 0, NULL, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation VALUES (193, NULL, 147372832245312, 153379497094688, 1, 0, 0, 153379496920608, 1, 0, NULL, '2025-09-30 14:52:36.978', '2025-09-30 14:52:36.98');
INSERT INTO public.t_ds_process_task_relation VALUES (196, NULL, 152317790975712, 152439162752320, 10, 0, 0, 152439162287424, 10, 0, NULL, '2025-09-30 15:22:37.592', '2025-09-30 15:22:37.593');
INSERT INTO public.t_ds_process_task_relation VALUES (198, NULL, 147372832245312, 153381689458208, 1, 0, 0, 153381689437728, 1, 0, NULL, '2025-09-30 15:30:35.563', '2025-09-30 15:30:35.563');
INSERT INTO public.t_ds_process_task_relation VALUES (200, NULL, 147372832245312, 153379745251872, 2, 0, 0, 153381999104544, 1, 0, NULL, '2025-09-30 15:36:00.293', '2025-09-30 15:36:00.295');
INSERT INTO public.t_ds_process_task_relation VALUES (203, NULL, 152317790975712, 152642396472640, 8, 0, 0, 152642396352832, 8, 0, NULL, '2025-09-30 15:38:29.553', '2025-09-30 15:38:29.553');
INSERT INTO public.t_ds_process_task_relation VALUES (204, NULL, 152317790975712, 152648927087936, 3, 0, 0, 152648927077696, 3, 0, NULL, '2025-09-30 15:39:23.617', '2025-09-30 15:39:23.617');
INSERT INTO public.t_ds_process_task_relation VALUES (205, NULL, 152317790975712, 152649931035968, 2, 0, 0, 152649930990912, 2, 0, NULL, '2025-09-30 15:41:18.34', '2025-09-30 15:41:18.341');
INSERT INTO public.t_ds_process_task_relation VALUES (207, NULL, 152317790975712, 152653935311168, 3, 0, 0, 152653935271232, 3, 0, NULL, '2025-09-30 15:44:07.474', '2025-09-30 15:44:07.474');
INSERT INTO public.t_ds_process_task_relation VALUES (208, NULL, 152317790975712, 152654997830976, 3, 0, 0, 152654997784896, 3, 0, NULL, '2025-09-30 15:45:32.525', '2025-09-30 15:45:32.525');
INSERT INTO public.t_ds_process_task_relation VALUES (210, NULL, 152317790975712, 152655297809728, 3, 0, 0, 152655297790272, 3, 0, NULL, '2025-09-30 15:47:19.52', '2025-09-30 15:47:19.52');
INSERT INTO public.t_ds_process_task_relation VALUES (211, NULL, 152317790975712, 152655501885760, 2, 0, 0, 152655501865280, 2, 0, NULL, '2025-09-30 15:48:35.669', '2025-09-30 15:48:35.669');
INSERT INTO public.t_ds_process_task_relation VALUES (214, NULL, 152317790975712, 152656044221760, 4, 0, 0, 152656044210496, 4, 0, NULL, '2025-09-30 15:50:50.1', '2025-09-30 15:50:50.101');
INSERT INTO public.t_ds_process_task_relation VALUES (216, NULL, 152317790975712, 152656278456640, 3, 0, 0, 152656278439232, 3, 0, NULL, '2025-09-30 15:56:31.305', '2025-09-30 15:56:31.306');
INSERT INTO public.t_ds_process_task_relation VALUES (217, NULL, 152317790975712, 152656664685888, 2, 0, 0, 152656664673600, 2, 0, NULL, '2025-09-30 15:57:29.437', '2025-09-30 15:57:29.437');
INSERT INTO public.t_ds_process_task_relation VALUES (218, NULL, 152317790975712, 152657219441984, 3, 0, 0, 152657219431744, 3, 0, NULL, '2025-09-30 16:00:15.176', '2025-09-30 16:00:15.176');
INSERT INTO public.t_ds_process_task_relation VALUES (220, NULL, 152317790975712, 152658250428736, 4, 0, 0, 152658250408256, 4, 0, NULL, '2025-09-30 16:03:29.98', '2025-09-30 16:03:29.98');
INSERT INTO public.t_ds_process_task_relation VALUES (221, NULL, 152317790975712, 152658885689664, 2, 0, 0, 152658885655872, 2, 0, NULL, '2025-09-30 16:05:39.372', '2025-09-30 16:05:39.373');
INSERT INTO public.t_ds_process_task_relation VALUES (223, NULL, 152317790975712, 153383838261792, 2, 0, 0, 153383838261792, 2, 0, NULL, '2025-09-30 16:09:14.44', '2025-09-30 16:09:14.44');


--
-- Data for Name: t_ds_process_task_relation_log; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_process_task_relation_log VALUES (2, NULL, 141883958809440, 150911519707360, 1, 0, 0, 150911519707360, 1, 0, NULL, 1, '2025-09-02 17:26:48.154', '2025-09-02 17:26:48.154', '2025-09-02 17:26:48.154');
INSERT INTO public.t_ds_process_task_relation_log VALUES (3, NULL, 141883958809440, 150911519707360, 2, 0, 0, 150911519707360, 2, 0, NULL, 1, '2025-09-02 17:31:15.673', '2025-09-02 17:31:15.673', '2025-09-02 17:31:15.673');
INSERT INTO public.t_ds_process_task_relation_log VALUES (5, NULL, 147372832245312, 150982603138816, 1, 0, 0, 150982603096832, 1, 0, NULL, 1, '2025-09-03 12:39:08.873', '2025-09-03 12:39:08.873', '2025-09-03 12:39:08.873');
INSERT INTO public.t_ds_process_task_relation_log VALUES (6, NULL, 147372832245312, 150982603138816, 2, 0, 0, 150982633305856, 1, 0, NULL, 1, '2025-09-03 12:39:38.381', '2025-09-03 12:39:38.381', '2025-09-03 12:39:38.381');
INSERT INTO public.t_ds_process_task_relation_log VALUES (46, NULL, 141883958809440, 152150248157120, 1, 0, 0, 152150248157120, 1, 0, NULL, 1, '2025-09-16 17:24:34.356', '2025-09-16 17:24:34.356', '2025-09-16 17:24:34.356');
INSERT INTO public.t_ds_process_task_relation_log VALUES (47, NULL, 134799536571008, 152150518585280, 1, 0, 0, 152150518559680, 1, 0, NULL, 1, '2025-09-16 17:28:10.812', '2025-09-16 17:28:10.812', '2025-09-16 17:28:10.812');
INSERT INTO public.t_ds_process_task_relation_log VALUES (48, NULL, 134799536571008, 152150518585280, 2, 0, 0, 152150534972352, 1, 0, NULL, 1, '2025-09-16 17:28:26.852', '2025-09-16 17:28:26.852', '2025-09-16 17:28:26.852');
INSERT INTO public.t_ds_process_task_relation_log VALUES (49, NULL, 147372832245312, 152150622981056, 1, 0, 0, 152150622975936, 1, 0, NULL, 1, '2025-09-16 17:29:52.759', '2025-09-16 17:29:52.759', '2025-09-16 17:29:52.759');
INSERT INTO public.t_ds_process_task_relation_log VALUES (50, NULL, 141883958809440, 152150699767744, 1, 0, 0, 152150699760576, 1, 0, NULL, 1, '2025-09-16 17:31:07.746', '2025-09-16 17:31:07.746', '2025-09-16 17:31:07.746');
INSERT INTO public.t_ds_process_task_relation_log VALUES (51, NULL, 141883958809440, 152150699767744, 2, 0, 0, 152150699760576, 2, 0, NULL, 1, '2025-09-16 17:31:49.226', '2025-09-16 17:31:49.226', '2025-09-16 17:31:49.226');
INSERT INTO public.t_ds_process_task_relation_log VALUES (52, NULL, 134799536571008, 152413381367104, 1, 0, 0, 152413380820288, 1, 0, NULL, 1, '2025-09-19 16:46:32.796', '2025-09-19 16:46:32.796', '2025-09-19 16:46:32.796');
INSERT INTO public.t_ds_process_task_relation_log VALUES (53, NULL, 134799536571008, 152413381367104, 2, 0, 0, 152413553931584, 1, 0, NULL, 1, '2025-09-19 16:49:29.259', '2025-09-19 16:49:29.259', '2025-09-19 16:49:29.259');
INSERT INTO public.t_ds_process_task_relation_log VALUES (54, NULL, 134799536571008, 152413555449152, 1, 0, 0, 152413555441984, 1, 0, NULL, 1, '2025-09-19 16:49:30.703', '2025-09-19 16:49:30.703', '2025-09-19 16:49:30.703');
INSERT INTO public.t_ds_process_task_relation_log VALUES (55, NULL, 134799536571008, 152413557117248, 1, 0, 0, 152413557107008, 1, 0, NULL, 1, '2025-09-19 16:49:32.334', '2025-09-19 16:49:32.334', '2025-09-19 16:49:32.334');
INSERT INTO public.t_ds_process_task_relation_log VALUES (56, NULL, 134799536571008, 152413579812160, 1, 0, 0, 152413579804992, 1, 0, NULL, 1, '2025-09-19 16:49:56.934', '2025-09-19 16:49:56.934', '2025-09-19 16:49:56.934');
INSERT INTO public.t_ds_process_task_relation_log VALUES (59, NULL, 147372832245312, 152416165530944, 1, 0, 0, 152416165498176, 1, 0, NULL, 1, '2025-09-19 17:35:06.714', '2025-09-19 17:35:06.714', '2025-09-19 17:35:06.714');
INSERT INTO public.t_ds_process_task_relation_log VALUES (60, NULL, 147372832245312, 152416165530944, 2, 0, 0, 152416173958464, 1, 0, NULL, 1, '2025-09-19 17:35:17.837', '2025-09-19 17:35:17.837', '2025-09-19 17:35:17.837');
INSERT INTO public.t_ds_process_task_relation_log VALUES (61, NULL, 152317790975712, 152439162752320, 1, 0, 0, 152439162287424, 1, 0, NULL, 1, '2025-09-20 00:09:32.218', '2025-09-20 00:09:32.218', '2025-09-20 00:09:32.218');
INSERT INTO public.t_ds_process_task_relation_log VALUES (62, NULL, 152317790975712, 152439162752320, 2, 0, 0, 152439162287424, 2, 0, NULL, 1, '2025-09-20 00:13:15.045', '2025-09-20 00:13:15.045', '2025-09-20 00:13:15.045');
INSERT INTO public.t_ds_process_task_relation_log VALUES (63, NULL, 152317790975712, 152439162752320, 3, 0, 0, 152439162287424, 3, 0, NULL, 1, '2025-09-20 00:21:50.385', '2025-09-20 00:21:50.385', '2025-09-20 00:21:50.385');
INSERT INTO public.t_ds_process_task_relation_log VALUES (64, NULL, 152317790975712, 152439162752320, 4, 0, 0, 152439162287424, 4, 0, NULL, 1, '2025-09-20 00:22:23.632', '2025-09-20 00:22:23.632', '2025-09-20 00:22:23.632');
INSERT INTO public.t_ds_process_task_relation_log VALUES (65, NULL, 152317790975712, 152439162752320, 5, 0, 0, 152439162287424, 5, 0, NULL, 1, '2025-09-20 00:22:31.419', '2025-09-20 00:22:31.419', '2025-09-20 00:22:31.419');
INSERT INTO public.t_ds_process_task_relation_log VALUES (66, NULL, 152317790975712, 152439162752320, 6, 0, 0, 152439162287424, 6, 0, NULL, 1, '2025-09-20 00:23:50.721', '2025-09-20 00:23:50.721', '2025-09-20 00:23:50.721');
INSERT INTO public.t_ds_process_task_relation_log VALUES (67, NULL, 152317790975712, 152642396472640, 1, 0, 0, 152642396352832, 1, 0, NULL, 1, '2025-09-22 09:25:52.419', '2025-09-22 09:25:52.419', '2025-09-22 09:25:52.419');
INSERT INTO public.t_ds_process_task_relation_log VALUES (68, NULL, 152317790975712, 152642396472640, 2, 0, 0, 152642396352832, 2, 0, NULL, 1, '2025-09-22 09:26:30.288', '2025-09-22 09:26:30.288', '2025-09-22 09:26:30.288');
INSERT INTO public.t_ds_process_task_relation_log VALUES (69, NULL, 152317790975712, 152439162752320, 7, 0, 0, 152439162287424, 7, 0, NULL, 1, '2025-09-22 09:26:58.956', '2025-09-22 09:26:58.956', '2025-09-22 09:26:58.956');
INSERT INTO public.t_ds_process_task_relation_log VALUES (70, NULL, 152317790975712, 152642396472640, 3, 0, 0, 152642396352832, 3, 0, NULL, 1, '2025-09-22 09:27:17.487', '2025-09-22 09:27:17.487', '2025-09-22 09:27:17.487');
INSERT INTO public.t_ds_process_task_relation_log VALUES (73, NULL, 152317790975712, 152648927087936, 1, 0, 0, 152648927077696, 1, 0, NULL, 1, '2025-09-22 11:19:31.102', '2025-09-22 11:19:31.102', '2025-09-22 11:19:31.102');
INSERT INTO public.t_ds_process_task_relation_log VALUES (74, NULL, 152317790975712, 152649931035968, 1, 0, 0, 152649930990912, 1, 0, NULL, 1, '2025-09-22 11:37:12.783', '2025-09-22 11:37:12.783', '2025-09-22 11:37:12.783');
INSERT INTO public.t_ds_process_task_relation_log VALUES (79, NULL, 152317790975712, 152653935311168, 1, 0, 0, 152653935271232, 1, 0, NULL, 1, '2025-09-22 12:46:47.649', '2025-09-22 12:46:47.649', '2025-09-22 12:46:47.649');
INSERT INTO public.t_ds_process_task_relation_log VALUES (80, NULL, 152317790975712, 152654997830976, 1, 0, 0, 152654997784896, 1, 0, NULL, 1, '2025-09-22 13:05:39.209', '2025-09-22 13:05:39.209', '2025-09-22 13:05:39.209');
INSERT INTO public.t_ds_process_task_relation_log VALUES (81, NULL, 152317790975712, 152648927087936, 2, 0, 0, 152648927077696, 2, 0, NULL, 1, '2025-09-22 13:06:02', '2025-09-22 13:06:02', '2025-09-22 13:06:02');
INSERT INTO public.t_ds_process_task_relation_log VALUES (82, NULL, 152317790975712, 152654997830976, 2, 0, 0, 152654997784896, 2, 0, NULL, 1, '2025-09-22 13:10:04.167', '2025-09-22 13:10:04.167', '2025-09-22 13:10:04.167');
INSERT INTO public.t_ds_process_task_relation_log VALUES (83, NULL, 152317790975712, 152655297809728, 1, 0, 0, 152655297790272, 1, 0, NULL, 1, '2025-09-22 13:10:50.243', '2025-09-22 13:10:50.243', '2025-09-22 13:10:50.243');
INSERT INTO public.t_ds_process_task_relation_log VALUES (84, NULL, 152317790975712, 152655501885760, 1, 0, 0, 152655501865280, 1, 0, NULL, 1, '2025-09-22 13:14:24.044', '2025-09-22 13:14:24.044', '2025-09-22 13:14:24.044');
INSERT INTO public.t_ds_process_task_relation_log VALUES (85, NULL, 152317790975712, 152656044221760, 1, 0, 0, 152656044210496, 1, 0, NULL, 1, '2025-09-22 13:23:43.692', '2025-09-22 13:23:43.692', '2025-09-22 13:23:43.692');
INSERT INTO public.t_ds_process_task_relation_log VALUES (86, NULL, 152317790975712, 152656278456640, 1, 0, 0, 152656278439232, 1, 0, NULL, 1, '2025-09-22 13:27:45.348', '2025-09-22 13:27:45.348', '2025-09-22 13:27:45.348');
INSERT INTO public.t_ds_process_task_relation_log VALUES (87, NULL, 152317790975712, 152656664685888, 1, 0, 0, 152656664673600, 1, 0, NULL, 1, '2025-09-22 13:34:27.714', '2025-09-22 13:34:27.714', '2025-09-22 13:34:27.714');
INSERT INTO public.t_ds_process_task_relation_log VALUES (88, NULL, 152317790975712, 152657219441984, 1, 0, 0, 152657219431744, 1, 0, NULL, 1, '2025-09-22 13:44:11.867', '2025-09-22 13:44:11.867', '2025-09-22 13:44:11.867');
INSERT INTO public.t_ds_process_task_relation_log VALUES (89, NULL, 152317790975712, 152657219441984, 2, 0, 0, 152657219431744, 2, 0, NULL, 1, '2025-09-22 13:44:26.205', '2025-09-22 13:44:26.205', '2025-09-22 13:44:26.205');
INSERT INTO public.t_ds_process_task_relation_log VALUES (92, NULL, 147372832245312, 152657963472192, 1, 0, 0, 152657963461952, 1, 0, NULL, 1, '2025-09-22 13:57:17.776', '2025-09-22 13:57:17.776', '2025-09-22 13:57:17.776');
INSERT INTO public.t_ds_process_task_relation_log VALUES (93, NULL, 152317790975712, 152658250428736, 1, 0, 0, 152658250408256, 1, 0, NULL, 1, '2025-09-22 14:02:24.042', '2025-09-22 14:02:24.042', '2025-09-22 14:02:24.042');
INSERT INTO public.t_ds_process_task_relation_log VALUES (94, NULL, 152317790975712, 152658250428736, 2, 0, 0, 152658250408256, 2, 0, NULL, 1, '2025-09-22 14:02:36.742', '2025-09-22 14:02:36.742', '2025-09-22 14:02:36.742');
INSERT INTO public.t_ds_process_task_relation_log VALUES (95, NULL, 152317790975712, 152658885689664, 1, 0, 0, 152658885655872, 1, 0, NULL, 1, '2025-09-22 14:13:36.311', '2025-09-22 14:13:36.311', '2025-09-22 14:13:36.311');
INSERT INTO public.t_ds_process_task_relation_log VALUES (104, NULL, 147372832245312, 152416165530944, 3, 0, 0, 152662937492800, 1, 0, NULL, 1, '2025-09-22 15:24:10.641', '2025-09-22 15:24:10.641', '2025-09-22 15:24:10.641');
INSERT INTO public.t_ds_process_task_relation_log VALUES (119, NULL, 147372832245312, 152665378283840, 1, 0, 0, 152665378277696, 1, 0, NULL, 1, '2025-09-22 16:07:25.883', '2025-09-22 16:07:25.883', '2025-09-22 16:07:25.883');
INSERT INTO public.t_ds_process_task_relation_log VALUES (145, NULL, 152317790975712, 152672440882496, 1, 0, 0, 152672440882496, 1, 0, NULL, 1, '2025-09-22 18:03:55.901', '2025-09-22 18:03:55.901', '2025-09-22 18:03:55.901');
INSERT INTO public.t_ds_process_task_relation_log VALUES (146, NULL, 152317790975712, 152672440882496, 2, 0, 0, 152672440882496, 2, 0, NULL, 1, '2025-09-22 18:06:41.808', '2025-09-22 18:06:41.808', '2025-09-22 18:06:41.808');
INSERT INTO public.t_ds_process_task_relation_log VALUES (149, NULL, 152317790975712, 152672766952768, 1, 0, 0, 152672766952768, 1, 0, NULL, 1, '2025-09-22 18:13:05.622', '2025-09-22 18:13:05.622', '2025-09-22 18:13:05.622');
INSERT INTO public.t_ds_process_task_relation_log VALUES (150, NULL, 152317790975712, 152673403135296, 1, 0, 0, 152673322082624, 1, 0, NULL, 1, '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236');
INSERT INTO public.t_ds_process_task_relation_log VALUES (151, NULL, 152317790975712, 152673403135296, 1, 152673322082624, 1, 152673327374656, 1, 0, NULL, 1, '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236');
INSERT INTO public.t_ds_process_task_relation_log VALUES (152, NULL, 152317790975712, 152673403135296, 1, 152673327374656, 1, 152673332505920, 1, 0, NULL, 1, '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236', '2025-09-22 18:19:24.236');
INSERT INTO public.t_ds_process_task_relation_log VALUES (153, NULL, 152317790975712, 152673403135296, 2, 0, 0, 152673322082624, 2, 0, NULL, 1, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation_log VALUES (154, NULL, 152317790975712, 152673403135296, 2, 152673322082624, 2, 152673327374656, 2, 0, NULL, 1, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation_log VALUES (155, NULL, 152317790975712, 152673403135296, 2, 152673327374656, 2, 152673332505920, 2, 0, NULL, 1, '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912', '2025-09-22 18:19:42.912');
INSERT INTO public.t_ds_process_task_relation_log VALUES (156, NULL, 152317790975712, 152673582845248, 1, 0, 0, 152673488823616, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (157, NULL, 152317790975712, 152673582845248, 1, 0, 0, 152673511663936, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (158, NULL, 152317790975712, 152673582845248, 1, 152673488823616, 1, 152673498672448, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (159, NULL, 152317790975712, 152673582845248, 1, 152673511663936, 1, 152673502172480, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (160, NULL, 152317790975712, 152673582845248, 1, 152673498672448, 1, 152673475740992, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (161, NULL, 152317790975712, 152673582845248, 1, 152673502172480, 1, 152673475740992, 1, 0, NULL, 1, '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737', '2025-09-22 18:22:19.737');
INSERT INTO public.t_ds_process_task_relation_log VALUES (162, NULL, 152317790975712, 152673582845248, 2, 0, 0, 152673488823616, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (163, NULL, 152317790975712, 152673582845248, 2, 0, 0, 152673511663936, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (164, NULL, 152317790975712, 152673582845248, 2, 152673488823616, 2, 152673498672448, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (165, NULL, 152317790975712, 152673582845248, 2, 152673511663936, 2, 152673502172480, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (166, NULL, 152317790975712, 152673582845248, 2, 152673498672448, 2, 152673475740992, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (167, NULL, 152317790975712, 152673582845248, 2, 152673502172480, 2, 152673475740992, 2, 0, NULL, 1, '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278', '2025-09-22 18:23:31.278');
INSERT INTO public.t_ds_process_task_relation_log VALUES (168, NULL, 152317790975712, 152673582845248, 3, 0, 0, 152673488823616, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (169, NULL, 152317790975712, 152673582845248, 3, 0, 0, 152673511663936, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (170, NULL, 152317790975712, 152673582845248, 3, 152673488823616, 3, 152673498672448, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (171, NULL, 152317790975712, 152673582845248, 3, 152673511663936, 3, 152673502172480, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (172, NULL, 152317790975712, 152673582845248, 3, 152673498672448, 3, 152673475740992, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (173, NULL, 152317790975712, 152673582845248, 3, 152673502172480, 3, 152673475740992, 3, 0, NULL, 1, '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456', '2025-09-22 18:23:38.456');
INSERT INTO public.t_ds_process_task_relation_log VALUES (174, NULL, 152317790975712, 152673582845248, 4, 0, 0, 152673488823616, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (175, NULL, 152317790975712, 152673582845248, 4, 0, 0, 152673511663936, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (176, NULL, 152317790975712, 152673582845248, 4, 152673488823616, 4, 152673498672448, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (177, NULL, 152317790975712, 152673582845248, 4, 152673511663936, 4, 152673502172480, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (178, NULL, 152317790975712, 152673582845248, 4, 152673498672448, 4, 152673475740992, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (179, NULL, 152317790975712, 152673582845248, 4, 152673502172480, 4, 152673475740992, 4, 0, NULL, 1, '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574', '2025-09-22 18:23:46.574');
INSERT INTO public.t_ds_process_task_relation_log VALUES (180, NULL, 152317790975712, 152673582845248, 5, 0, 0, 152673488823616, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (181, NULL, 152317790975712, 152673582845248, 5, 0, 0, 152673511663936, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (182, NULL, 152317790975712, 152673582845248, 5, 152673488823616, 5, 152673498672448, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (183, NULL, 152317790975712, 152673582845248, 5, 152673511663936, 5, 152673502172480, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (184, NULL, 152317790975712, 152673582845248, 5, 152673498672448, 5, 152673475740992, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (185, NULL, 152317790975712, 152673582845248, 5, 152673502172480, 5, 152673475740992, 5, 0, NULL, 1, '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587', '2025-09-22 18:23:52.587');
INSERT INTO public.t_ds_process_task_relation_log VALUES (192, NULL, 152317790975712, 152439162752320, 8, 0, 0, 152439162287424, 8, 0, NULL, 1, '2025-09-30 14:34:20.06', '2025-09-30 14:34:20.06', '2025-09-30 14:34:20.06');
INSERT INTO public.t_ds_process_task_relation_log VALUES (193, NULL, 147372832245312, 153379497094688, 1, 0, 0, 153379496920608, 1, 0, NULL, 1, '2025-09-30 14:52:36.978', '2025-09-30 14:52:36.978', '2025-09-30 14:52:36.978');
INSERT INTO public.t_ds_process_task_relation_log VALUES (194, NULL, 147372832245312, 153379745251872, 1, 0, 0, 153379745225248, 1, 0, NULL, 1, '2025-09-30 14:56:53.408', '2025-09-30 14:56:53.408', '2025-09-30 14:56:53.408');
INSERT INTO public.t_ds_process_task_relation_log VALUES (195, NULL, 152317790975712, 152439162752320, 9, 0, 0, 152439162287424, 9, 0, NULL, 1, '2025-09-30 15:20:03.013', '2025-09-30 15:20:03.013', '2025-09-30 15:20:03.013');
INSERT INTO public.t_ds_process_task_relation_log VALUES (196, NULL, 152317790975712, 152439162752320, 10, 0, 0, 152439162287424, 10, 0, NULL, 1, '2025-09-30 15:22:37.592', '2025-09-30 15:22:37.592', '2025-09-30 15:22:37.592');
INSERT INTO public.t_ds_process_task_relation_log VALUES (197, NULL, 152317790975712, 152642396472640, 4, 0, 0, 152642396352832, 4, 0, NULL, 1, '2025-09-30 15:26:33.756', '2025-09-30 15:26:33.756', '2025-09-30 15:26:33.756');
INSERT INTO public.t_ds_process_task_relation_log VALUES (198, NULL, 147372832245312, 153381689458208, 1, 0, 0, 153381689437728, 1, 0, NULL, 1, '2025-09-30 15:30:35.563', '2025-09-30 15:30:35.563', '2025-09-30 15:30:35.563');
INSERT INTO public.t_ds_process_task_relation_log VALUES (199, NULL, 152317790975712, 152642396472640, 5, 0, 0, 152642396352832, 5, 0, NULL, 1, '2025-09-30 15:32:21.241', '2025-09-30 15:32:21.241', '2025-09-30 15:32:21.241');
INSERT INTO public.t_ds_process_task_relation_log VALUES (200, NULL, 147372832245312, 153379745251872, 2, 0, 0, 153381999104544, 1, 0, NULL, 1, '2025-09-30 15:36:00.293', '2025-09-30 15:36:00.293', '2025-09-30 15:36:00.293');
INSERT INTO public.t_ds_process_task_relation_log VALUES (201, NULL, 152317790975712, 152642396472640, 6, 0, 0, 152642396352832, 6, 0, NULL, 1, '2025-09-30 15:36:29.861', '2025-09-30 15:36:29.861', '2025-09-30 15:36:29.861');
INSERT INTO public.t_ds_process_task_relation_log VALUES (202, NULL, 152317790975712, 152642396472640, 7, 0, 0, 152642396352832, 7, 0, NULL, 1, '2025-09-30 15:38:05.94', '2025-09-30 15:38:05.94', '2025-09-30 15:38:05.94');
INSERT INTO public.t_ds_process_task_relation_log VALUES (203, NULL, 152317790975712, 152642396472640, 8, 0, 0, 152642396352832, 8, 0, NULL, 1, '2025-09-30 15:38:29.553', '2025-09-30 15:38:29.553', '2025-09-30 15:38:29.553');
INSERT INTO public.t_ds_process_task_relation_log VALUES (204, NULL, 152317790975712, 152648927087936, 3, 0, 0, 152648927077696, 3, 0, NULL, 1, '2025-09-30 15:39:23.617', '2025-09-30 15:39:23.617', '2025-09-30 15:39:23.617');
INSERT INTO public.t_ds_process_task_relation_log VALUES (205, NULL, 152317790975712, 152649931035968, 2, 0, 0, 152649930990912, 2, 0, NULL, 1, '2025-09-30 15:41:18.34', '2025-09-30 15:41:18.34', '2025-09-30 15:41:18.34');
INSERT INTO public.t_ds_process_task_relation_log VALUES (206, NULL, 152317790975712, 152653935311168, 2, 0, 0, 152653935271232, 2, 0, NULL, 1, '2025-09-30 15:43:52.625', '2025-09-30 15:43:52.625', '2025-09-30 15:43:52.625');
INSERT INTO public.t_ds_process_task_relation_log VALUES (207, NULL, 152317790975712, 152653935311168, 3, 0, 0, 152653935271232, 3, 0, NULL, 1, '2025-09-30 15:44:07.474', '2025-09-30 15:44:07.474', '2025-09-30 15:44:07.474');
INSERT INTO public.t_ds_process_task_relation_log VALUES (208, NULL, 152317790975712, 152654997830976, 3, 0, 0, 152654997784896, 3, 0, NULL, 1, '2025-09-30 15:45:32.525', '2025-09-30 15:45:32.525', '2025-09-30 15:45:32.525');
INSERT INTO public.t_ds_process_task_relation_log VALUES (209, NULL, 152317790975712, 152655297809728, 2, 0, 0, 152655297790272, 2, 0, NULL, 1, '2025-09-30 15:47:08.563', '2025-09-30 15:47:08.563', '2025-09-30 15:47:08.563');
INSERT INTO public.t_ds_process_task_relation_log VALUES (210, NULL, 152317790975712, 152655297809728, 3, 0, 0, 152655297790272, 3, 0, NULL, 1, '2025-09-30 15:47:19.52', '2025-09-30 15:47:19.52', '2025-09-30 15:47:19.52');
INSERT INTO public.t_ds_process_task_relation_log VALUES (211, NULL, 152317790975712, 152655501885760, 2, 0, 0, 152655501865280, 2, 0, NULL, 1, '2025-09-30 15:48:35.669', '2025-09-30 15:48:35.669', '2025-09-30 15:48:35.669');
INSERT INTO public.t_ds_process_task_relation_log VALUES (212, NULL, 152317790975712, 152656044221760, 2, 0, 0, 152656044210496, 2, 0, NULL, 1, '2025-09-30 15:50:31.863', '2025-09-30 15:50:31.863', '2025-09-30 15:50:31.863');
INSERT INTO public.t_ds_process_task_relation_log VALUES (213, NULL, 152317790975712, 152656044221760, 3, 0, 0, 152656044210496, 3, 0, NULL, 1, '2025-09-30 15:50:38.669', '2025-09-30 15:50:38.669', '2025-09-30 15:50:38.669');
INSERT INTO public.t_ds_process_task_relation_log VALUES (214, NULL, 152317790975712, 152656044221760, 4, 0, 0, 152656044210496, 4, 0, NULL, 1, '2025-09-30 15:50:50.1', '2025-09-30 15:50:50.1', '2025-09-30 15:50:50.1');
INSERT INTO public.t_ds_process_task_relation_log VALUES (215, NULL, 152317790975712, 152656278456640, 2, 0, 0, 152656278439232, 2, 0, NULL, 1, '2025-09-30 15:55:28.768', '2025-09-30 15:55:28.768', '2025-09-30 15:55:28.768');
INSERT INTO public.t_ds_process_task_relation_log VALUES (216, NULL, 152317790975712, 152656278456640, 3, 0, 0, 152656278439232, 3, 0, NULL, 1, '2025-09-30 15:56:31.305', '2025-09-30 15:56:31.305', '2025-09-30 15:56:31.305');
INSERT INTO public.t_ds_process_task_relation_log VALUES (217, NULL, 152317790975712, 152656664685888, 2, 0, 0, 152656664673600, 2, 0, NULL, 1, '2025-09-30 15:57:29.437', '2025-09-30 15:57:29.437', '2025-09-30 15:57:29.437');
INSERT INTO public.t_ds_process_task_relation_log VALUES (218, NULL, 152317790975712, 152657219441984, 3, 0, 0, 152657219431744, 3, 0, NULL, 1, '2025-09-30 16:00:15.176', '2025-09-30 16:00:15.176', '2025-09-30 16:00:15.176');
INSERT INTO public.t_ds_process_task_relation_log VALUES (219, NULL, 152317790975712, 152658250428736, 3, 0, 0, 152658250408256, 3, 0, NULL, 1, '2025-09-30 16:03:07.123', '2025-09-30 16:03:07.123', '2025-09-30 16:03:07.123');
INSERT INTO public.t_ds_process_task_relation_log VALUES (220, NULL, 152317790975712, 152658250428736, 4, 0, 0, 152658250408256, 4, 0, NULL, 1, '2025-09-30 16:03:29.98', '2025-09-30 16:03:29.98', '2025-09-30 16:03:29.98');
INSERT INTO public.t_ds_process_task_relation_log VALUES (221, NULL, 152317790975712, 152658885689664, 2, 0, 0, 152658885655872, 2, 0, NULL, 1, '2025-09-30 16:05:39.372', '2025-09-30 16:05:39.372', '2025-09-30 16:05:39.372');
INSERT INTO public.t_ds_process_task_relation_log VALUES (222, NULL, 152317790975712, 153383838261792, 1, 0, 0, 153383838261792, 1, 0, NULL, 1, '2025-09-30 16:08:34.038', '2025-09-30 16:08:34.038', '2025-09-30 16:08:34.038');
INSERT INTO public.t_ds_process_task_relation_log VALUES (223, NULL, 152317790975712, 153383838261792, 2, 0, 0, 153383838261792, 2, 0, NULL, 1, '2025-09-30 16:09:14.44', '2025-09-30 16:09:14.44', '2025-09-30 16:09:14.44');


--
-- Data for Name: t_ds_project; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_project VALUES (2, '数据发现', 134799536571008, '', 1, 1, '2025-03-04 14:18:46.664', '2025-05-23 16:29:10.094');
INSERT INTO public.t_ds_project VALUES (3, '数据质量', 147372832245312, '', 1, 1, '2025-03-04 14:18:46.664', '2025-05-23 16:29:10.094');
INSERT INTO public.t_ds_project VALUES (1, '默认项目组', 141883958809440, 'qData系统默认项目组', 1, 1, '2025-03-04 14:18:46.664', '2025-09-19 09:34:52.628');
INSERT INTO public.t_ds_project VALUES (35, '监测数据组', 152317955043040, '监测数据处理项目组，聚焦项目运行中的设备与系统监测数据（如传感器、日志、性能指标），负责本类数据的采集接入、清洗转换、模型构建与统一管理，支撑状态监控、异常预警及运维分析类数据服务。', 1, 1, '2025-09-18 14:53:28.949', '2025-09-22 19:21:26.121');
INSERT INTO public.t_ds_project VALUES (34, '基础数据组', 152317790975712, '基础数据处理项目组，用于统一开展项目内各类基础数据的采集、治理与清洗工作，保障数据质量与一致性。', 1, 1, '2025-09-18 14:50:42.706', '2025-09-22 19:21:31.721');
INSERT INTO public.t_ds_project VALUES (36, '业务数据组', 152317966055136, '业务数据处理项目组，围绕水利项目核心业务流程，负责水量调度、工程巡检、防汛工单、水文记录等结构化数据的汇聚整合、标准治理与加工处理，支撑业务统计、过程追溯与管理决策类数据应用。', 1, 1, '2025-09-18 14:53:39.703', '2025-09-22 19:21:35.701');


--
-- Data for Name: t_ds_project_parameter; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_project_preference; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_queue; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_queue VALUES (1, 'default', 'default', '2018-11-29 10:22:33', '2018-11-29 10:22:33');


--
-- Data for Name: t_ds_relation_datasource_user; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_namespace_user; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_process_instance; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_project_user; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_project_worker_group; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_resources_user; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_rule_execute_sql; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (1, 1, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (3, 5, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (2, 3, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (4, 3, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (5, 6, 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (6, 6, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (7, 7, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (8, 7, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (9, 8, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (10, 8, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (11, 9, 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (12, 9, 14, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (13, 10, 15, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (14, 1, 16, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_execute_sql VALUES (15, 5, 17, '2021-03-03 11:31:24', '2021-03-03 11:31:24');


--
-- Data for Name: t_ds_relation_rule_input_entry; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_relation_rule_input_entry VALUES (1, 1, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (2, 1, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (3, 1, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (4, 1, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (5, 1, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (6, 1, 6, '{"statistics_name":"null_count.nulls"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (7, 1, 7, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (8, 1, 8, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (9, 1, 9, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (10, 1, 10, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (11, 1, 17, '', 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (12, 1, 19, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (13, 2, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (14, 2, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (15, 2, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (16, 2, 6, '{"is_show":"true","can_edit":"true"}', 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (17, 2, 16, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (18, 2, 4, NULL, 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (19, 2, 7, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (20, 2, 8, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (21, 2, 9, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (22, 2, 10, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (24, 2, 19, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (25, 3, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (26, 3, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (27, 3, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (28, 3, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (29, 3, 11, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (30, 3, 12, NULL, 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (31, 3, 13, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (32, 3, 14, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (33, 3, 15, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (34, 3, 7, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (35, 3, 8, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (36, 3, 9, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (37, 3, 10, NULL, 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (38, 3, 17, '{"comparison_name":"total_count.total"}', 14, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (39, 3, 19, NULL, 15, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (40, 4, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (41, 4, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (42, 4, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (43, 4, 6, '{"is_show":"true","can_edit":"true"}', 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (44, 4, 16, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (45, 4, 11, NULL, 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (46, 4, 12, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (47, 4, 13, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (48, 4, 17, '{"is_show":"true","can_edit":"true"}', 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (49, 4, 18, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (50, 4, 7, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (51, 4, 8, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (52, 4, 9, NULL, 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (53, 4, 10, NULL, 14, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (62, 3, 6, '{"statistics_name":"miss_count.miss"}', 18, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (63, 5, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (64, 5, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (65, 5, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (66, 5, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (67, 5, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (68, 5, 6, '{"statistics_name":"invalid_length_count.valids"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (69, 5, 24, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (70, 5, 23, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (71, 5, 7, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (72, 5, 8, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (73, 5, 9, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (74, 5, 10, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (75, 5, 17, '', 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (76, 5, 19, NULL, 14, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (79, 6, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (80, 6, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (81, 6, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (82, 6, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (83, 6, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (84, 6, 6, '{"statistics_name":"duplicate_count.duplicates"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (85, 6, 7, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (86, 6, 8, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (87, 6, 9, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (88, 6, 10, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (89, 6, 17, '', 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (90, 6, 19, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (93, 7, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (94, 7, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (95, 7, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (96, 7, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (97, 7, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (98, 7, 6, '{"statistics_name":"regexp_count.regexps"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (99, 7, 25, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (100, 7, 7, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (101, 7, 8, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (102, 7, 9, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (103, 7, 10, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (104, 7, 17, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (105, 7, 19, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (108, 8, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (109, 8, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (110, 8, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (111, 8, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (112, 8, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (113, 8, 6, '{"statistics_name":"timeliness_count.timeliness"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (114, 8, 26, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (115, 8, 27, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (116, 8, 7, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (117, 8, 8, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (118, 8, 9, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (119, 8, 10, NULL, 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (120, 8, 17, NULL, 14, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (121, 8, 19, NULL, 15, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (124, 9, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (125, 9, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (126, 9, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (127, 9, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (128, 9, 5, NULL, 5, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (129, 9, 6, '{"statistics_name":"enum_count.enums"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (130, 9, 28, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (131, 9, 7, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (132, 9, 8, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (133, 9, 9, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (134, 9, 10, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (135, 9, 17, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (136, 9, 19, NULL, 13, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (139, 10, 1, NULL, 1, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (140, 10, 2, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (141, 10, 3, NULL, 3, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (142, 10, 4, NULL, 4, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (143, 10, 6, '{"statistics_name":"table_count.total"}', 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (144, 10, 7, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (145, 10, 8, NULL, 8, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (146, 10, 9, NULL, 9, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (147, 10, 10, NULL, 10, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (148, 10, 17, NULL, 11, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (149, 10, 19, NULL, 12, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (150, 8, 29, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (151, 1, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (152, 2, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (153, 3, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (154, 4, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (155, 5, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (156, 6, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (157, 7, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (158, 8, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (159, 9, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (160, 10, 30, NULL, 2, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (161, 3, 31, NULL, 6, '2021-03-03 11:31:24', '2021-03-03 11:31:24');
INSERT INTO public.t_ds_relation_rule_input_entry VALUES (162, 4, 31, NULL, 7, '2021-03-03 11:31:24', '2021-03-03 11:31:24');


--
-- Data for Name: t_ds_relation_sub_workflow; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_relation_udfs_user; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_resources; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_schedules; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_schedules VALUES (1, 150982603138816, '2025-09-03 12:39:38', '2125-08-10 12:39:38', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-03 12:39:08.996', '2025-09-03 12:39:38.459');
INSERT INTO public.t_ds_schedules VALUES (2, 152150518585280, '2025-09-16 17:28:26', '2125-08-23 17:28:26', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-16 17:28:10.898', '2025-09-16 17:28:26.91');
INSERT INTO public.t_ds_schedules VALUES (3, 152150622981056, '2025-09-16 17:29:52', '2125-08-23 17:29:52', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-16 17:29:52.791', '2025-09-16 17:29:52.791');
INSERT INTO public.t_ds_schedules VALUES (4, 152413381367104, '2025-09-19 16:49:29', '2125-08-26 16:49:29', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-19 16:46:33.103', '2025-09-19 16:49:29.302');
INSERT INTO public.t_ds_schedules VALUES (5, 152413555449152, '2025-09-19 16:49:30', '2125-08-26 16:49:30', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-19 16:49:30.747', '2025-09-19 16:49:30.747');
INSERT INTO public.t_ds_schedules VALUES (6, 152413557117248, '2025-09-19 16:49:32', '2125-08-26 16:49:32', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-19 16:49:32.38', '2025-09-19 16:49:32.38');
INSERT INTO public.t_ds_schedules VALUES (7, 152413579812160, '2025-09-19 16:49:56', '2125-08-26 16:49:56', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-19 16:49:56.967', '2025-09-19 16:49:56.967');
INSERT INTO public.t_ds_schedules VALUES (14, 152657963472192, '2025-09-22 13:57:17', '2125-08-29 13:57:17', 'Asia/Shanghai', '0 0 0 * * ?', 1, 1, 0, 0, 1, 2, 'default', 'default', -1, '2025-09-22 13:57:17.819', '2025-09-22 13:57:17.819');
INSERT INTO public.t_ds_schedules VALUES (10, 152416165530944, '2025-09-22 15:24:10', '2125-08-29 15:24:10', 'Asia/Shanghai', '0 0 0 * * ?', 1, 1, 0, 0, 1, 2, 'default', 'default', -1, '2025-09-19 17:35:06.789', '2025-09-22 15:24:10.674');
INSERT INTO public.t_ds_schedules VALUES (15, 152665378283840, '2025-09-22 16:07:25', '2125-08-29 16:07:25', 'Asia/Shanghai', '0 0 0 * * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-22 16:07:25.912', '2025-09-22 16:07:25.912');
INSERT INTO public.t_ds_schedules VALUES (18, 153381689458208, '2025-09-30 15:30:35', '2125-09-06 15:30:35', 'Asia/Shanghai', '0 0 0 * * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-30 15:30:35.603', '2025-09-30 15:30:35.603');
INSERT INTO public.t_ds_schedules VALUES (17, 153379745251872, '2025-09-30 15:36:00', '2125-09-06 15:36:00', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 1, 0, 1, 2, 'default', 'default', -1, '2025-09-30 14:56:53.484', '2025-09-30 15:36:00.37');
INSERT INTO public.t_ds_schedules VALUES (16, 153379497094688, '2025-09-30 14:52:37', '2125-09-06 14:52:37', 'Asia/Shanghai', '0 0 0 1 * ?', 1, 1, 0, 0, 1, 2, 'default', 'default', -1, '2025-09-30 14:52:37.068', '2025-09-30 14:52:37.068');


--
-- Data for Name: t_ds_session; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_session VALUES ('d1afb0ba-7946-4c14-b3af-50a828064c58', 1, NULL, '2025-09-02 17:26:56.175');


--
-- Data for Name: t_ds_task_definition; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_task_definition VALUES (2, 150911519707360, '测试111', 2, '', 141883958809440, 1, 'FLINK', 0, '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;"}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-02 17:26:48.117', '2025-09-02 17:31:15.642');
INSERT INTO public.t_ds_task_definition VALUES (4, 150982603096832, '测试JEoZH8A1756874348816', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-03 12:39:08.862', '2025-09-03 12:39:08.862');
INSERT INTO public.t_ds_task_definition VALUES (5, 150982633305856, '测试9LMXA6X1756874378318', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-03 12:39:38.364', '2025-09-03 12:39:38.364');
INSERT INTO public.t_ds_task_definition VALUES (57, 152416173958464, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 17:35:17.825', '2025-09-19 17:35:17.825');
INSERT INTO public.t_ds_task_definition VALUES (61, 152648830391616, '站点经纬度范围与精度稽查VDFyo9i1758511069419', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":6,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/6","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:17:49.427', '2025-09-22 11:17:49.427');
INSERT INTO public.t_ds_task_definition VALUES (65, 152650259792192, '水位数值范围稽查aD3EDzj1758512581368', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":2,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/2","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:43:01.376', '2025-09-22 11:43:01.376');
INSERT INTO public.t_ds_task_definition VALUES (60, 152648200501568, '水位重复记录稽查JBmDEZ61758510409845', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":7,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/7","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:06:49.9', '2025-09-22 11:06:49.9');
INSERT INTO public.t_ds_task_definition VALUES (66, 152650261382464, '数据完整性稽查GZHrUJx1758512582924', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/4","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:43:02.931', '2025-09-22 11:43:02.931');
INSERT INTO public.t_ds_task_definition VALUES (68, 152653935271232, '转化_站点数据去重-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTEzNjI4NDgwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiV0FURVJfVFAiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6IldBVEVSX1RQIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6OCwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTAxMTY2Mjg4MDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9DT0RFIiwiU1RBVElPTl9OQU1FIiwiQkFTSU5fQ09ERSIsIlJJVkVSX05BTUUiLCJMT05HSVRVREUiLCJMQVRJVFVERSIsIkFETUlOX1JFR0lPTl9DT0RFIiwiU1RBVFVTIiwiQ1JFQVRFRF9BVCIsIlVQREFURURfQVQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX1NUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSIsIlNUQVRJT05fTkFNRSIsIkJBU0lOX0NPREUiLCJSSVZFUl9OQU1FIiwiTE9OR0lUVURFIiwiTEFUSVRVREUiLCJBRE1JTl9SRUdJT05fQ09ERSIsIlNUQVRVUyIsIkNSRUFURURfQVQiLCJVUERBVEVEX0FUIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoib2RzIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTAxMTE4NjgyMjQiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueaVsOaNruWOu+mHjSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLovazmjaLnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMzEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MzM4MjM5MjU0ODg5NiIsInBhcmFtZXRlciI6eyJ0YWJsZUZpZWxkcyI6W3sibmFtZSI6IuagueaNrmlk5ZKM57yW56CB5Y676YeNIiwicnVsZU5hbWUiOiLmjInnu4TlkIjlrZfmrrXljrvph43vvIjkv53nlZnmnIDmlrDmiJbpppbmnaHvvIkiLCJydWxlQ29kZSI6IjAyOSIsInN0YXR1cyI6IjEiLCJ3aGVyZUNsYXVzZSI6IiIsImNvbHVtbnMiOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzOSIsInJ1bGVDb25maWciOiJ7XCJjb2x1bW5zXCI6W1wiU1RBVElPTl9JRFwiLFwiU1RBVElPTl9DT0RFXCJdLFwic3RyaW5nVmFsdWVcIjpbe1wic29ydFwiOjEsXCJjb2x1bW5zXCI6XCJTVEFUSU9OX0lEXCIsXCJ0eXBlXCI6XCIwXCJ9XSxcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIumHjeWkjeiusOW9leWIoOmZpOexu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJLRUVQX0xBVEVTVF9PUl9GSVJTVCIsInBhcmVudE5hbWUiOiLph43lpI3orrDlvZXliKDpmaTnsbsifV0sIndoZXJlIjoiIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 12:46:47.632', '2025-09-30 15:44:07.456');
INSERT INTO public.t_ds_task_definition VALUES (62, 152648927077696, '转化_水位行转列-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0Mjc4MDMxMDg0OCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6KGM6L2s5YiXIiwidGFza1ZlcnNpb24iOjN9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:19:31.097', '2025-09-30 15:39:23.597');
INSERT INTO public.t_ds_task_definition VALUES (59, 152642396352832, '转化_水位列转行-2025-09-22', 8, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl0sImRiVHlwZSI6IkRNOCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1kd2QiLCJ0YWJsZSI6IkRXRF9TVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVF9XSURFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjR9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N5YiX6L2s6KGMIiwidGFza1ZlcnNpb24iOjh9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:38:29.538');
INSERT INTO public.t_ds_task_definition VALUES (69, 152654997784896, '多表_更新写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc2MzIxMDg4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc4MTM2NjQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfVVBEQVRFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIkJBU0lOX0NPREUiLCJTVEFUX01PTlRIIiwiU1RBVElPTl9DTlQiLCJBVkdfTEVWRUwiLCJNQVhfTEVWRUwiLCJNSU5fTEVWRUwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NDQ3MzM4MDE2MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf5pu05paw5YaZIiwidGFza1ZlcnNpb24iOjN9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:05:39.183', '2025-09-30 15:45:32.483');
INSERT INTO public.t_ds_task_definition VALUES (70, 152655297790272, '多表_全量写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjYzNjUwMTEyIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjY2Mzg0MTkyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfRlVMTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJCQVNJTl9DT0RFIiwiU1RBVF9NT05USCIsIlNUQVRJT05fQ05UIiwiQVZHX0xFVkVMIiwiTUFYX0xFVkVMIiwiTUlOX0xFVkVMIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoiYWRzIn0sIm5vZGVWZXJzaW9uIjozfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTUyMzg2NzQ3NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5aSa6KGoX+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:10:50.235', '2025-09-30 15:47:19.506');
INSERT INTO public.t_ds_task_definition VALUES (71, 152655501865280, '多表_追加写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NDI2MjMyNjQwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NDI4MjIwMjI0IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfQVBQRU5EIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIkJBU0lOX0NPREUiLCJTVEFUX01PTlRIIiwiU1RBVElPTl9DTlQiLCJBVkdfTEVWRUwiLCJNQVhfTEVWRUwiLCJNSU5fTEVWRUwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTQyNDg1MzMxMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf6L+95Yqg5YaZIiwidGFza1ZlcnNpb24iOjJ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:14:24.026', '2025-09-30 15:48:35.655');
INSERT INTO public.t_ds_task_definition VALUES (44, 152150248157120, '232332', 1, '', 141883958809440, 1, 'FLINK', 0, '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;"}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-16 17:24:34.34', '2025-09-16 17:24:34.34');
INSERT INTO public.t_ds_task_definition VALUES (45, 152150518559680, '232332JLZSD3A1758014890777', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-16 17:28:10.806', '2025-09-16 17:28:10.806');
INSERT INTO public.t_ds_task_definition VALUES (46, 152150534972352, '232332Cjy7WIv1758014906806', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-16 17:28:26.839', '2025-09-16 17:28:26.839');
INSERT INTO public.t_ds_task_definition VALUES (47, 152150622975936, '2323_user_20250916172917OMdTTDk1758014992747', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-16 17:29:52.755', '2025-09-16 17:29:52.755');
INSERT INTO public.t_ds_task_definition VALUES (48, 152150699760576, '1212-2025-09-16', 2, '', 141883958809440, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgxMTkxMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6InRlc3QiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsIm5hbWUiXSwiZGJUeXBlIjoiTXlTcWwiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlcl9jb3B5MSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgyOTgzMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IlRBWWoyVWR5cytFR2o5aGpJS3JiS1E9PSIsImRibmFtZSI6InRlc3QifSwiZGJOYW1lIjoidGVzdCIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6InRvcGRlbW8uY24iLCJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInBvcnQiOjMzMDYsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL3RvcGRlbW8uY246MzMwNi90ZXN0P3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJ1c2VyIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwibmFtZSJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyMTUwNjc5NDgxMjgwIiwicHJvamVjdENvZGUiOjE0MTg4Mzk1ODgwOTQ0MCwibmFtZSI6IjEyMTIiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4MTE5MTM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vdG9wZGVtby5jbjozMzA2L3Rlc3Q/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6InVzZXJfY29weTEifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4Mjk4MzM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJyb290IiwicGFzc3dvcmQiOiJUQVlqMlVkeXMrRUdqOWhqSUtyYktRPT0iLCJkYm5hbWUiOiJ0ZXN0In0sImRiTmFtZSI6InRlc3QiLCJkYlR5cGUiOiJNeVNxbCIsImhvc3QiOiJ0b3BkZW1vLmNuIiwicGFzc3dvcmQiOiJ3YW5nbWluZzExMTQiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwiZGJOYW1lIjoidGVzdCIsImRhdGFzb3VyY2VJZCI6MSwiY29sdW1uIjpbImlkIiwibmFtZSJdLCJkYlR5cGUiOiJNeVNxbCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlciJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsIm5hbWUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfV0sInRyYW5zaXRpb24iOltdfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-16 17:31:07.742', '2025-09-16 17:31:49.207');
INSERT INTO public.t_ds_task_definition VALUES (49, 152413380820288, '工程监管数据发现uVOWKbY1758271591783', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 16:46:32.742', '2025-09-19 16:46:32.742');
INSERT INTO public.t_ds_task_definition VALUES (50, 152413553931584, '工程监管数据发现wSAxLkg1758271769213', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 16:49:29.242', '2025-09-19 16:49:29.242');
INSERT INTO public.t_ds_task_definition VALUES (51, 152413555441984, '水文监测数据发现Rg0XxWU1758271770687', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":2,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/2","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 16:49:30.698', '2025-09-19 16:49:30.698');
INSERT INTO public.t_ds_task_definition VALUES (52, 152413557107008, '水质监测数据发现OemKK9R1758271772314', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/3","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 16:49:32.327', '2025-09-19 16:49:32.327');
INSERT INTO public.t_ds_task_definition VALUES (53, 152413579804992, '水资源数据发现xAxpna41758271796919', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/4","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 16:49:56.929', '2025-09-19 16:49:56.929');
INSERT INTO public.t_ds_task_definition VALUES (54, 152414754541888, '站点编码非空与格式稽查t0GWyOL1758273033134', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 17:10:33.202', '2025-09-19 17:10:33.202');
INSERT INTO public.t_ds_task_definition VALUES (55, 152414792186176, '水位数值范围稽查Vgs9m9g1758273072566', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":2,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/2","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 17:11:12.582', '2025-09-19 17:11:12.582');
INSERT INTO public.t_ds_task_definition VALUES (56, 152416165498176, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-19 17:35:06.691', '2025-09-19 17:35:06.691');
INSERT INTO public.t_ds_task_definition VALUES (76, 152657657668928, '数据完整性稽查huxkK4P1758520316836', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/4","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:51:56.887', '2025-09-22 13:51:56.887');
INSERT INTO public.t_ds_task_definition VALUES (77, 152657719954752, '数据完整性稽查HzqLIag1758520382690', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/4","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:53:02.704', '2025-09-22 13:53:02.704');
INSERT INTO public.t_ds_task_definition VALUES (78, 152657963461952, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/3","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:57:17.76', '2025-09-22 13:57:17.76');
INSERT INTO public.t_ds_task_definition VALUES (83, 152662937492800, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 15:24:10.631', '2025-09-22 15:24:10.631');
INSERT INTO public.t_ds_task_definition VALUES (84, 152665378277696, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 16:07:25.877', '2025-09-22 16:07:25.877');
INSERT INTO public.t_ds_task_definition VALUES (106, 153381689437728, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":12,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/12","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-30 15:30:35.551', '2025-09-30 15:30:35.551');
INSERT INTO public.t_ds_task_definition VALUES (58, 152439162287424, '转化_水位预警等级校验-2025-09-20', 10, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI0Mzg5NjYxMzQwODAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiYWRzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfU1RBVElPTl9XQVRFUl9MRVZFTF9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjZ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjQxNjE0NTQ2MzYxNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6aKE6K2m562J57qn5qCh6aqMIiwidGFza1ZlcnNpb24iOjEwfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-20 00:09:32.174', '2025-09-30 15:22:37.561');
INSERT INTO public.t_ds_task_definition VALUES (63, 152649930990912, '转化_站点表字符串拼接-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ5NzM4NDY4NjcyIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiV0FURVJfVFAiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6IldBVEVSX1RQIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6OCwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDk3NDEwOTgzMDQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9DT0RFIiwiU1RBVElPTl9OQU1FIiwiQkFTSU5fQ09ERSIsIlJJVkVSX05BTUUiLCJMT05HSVRVREUiLCJMQVRJVFVERSIsIkFETUlOX1JFR0lPTl9DT0RFIiwiU1RBVFVTIiwiQ1JFQVRFRF9BVCIsIlVQREFURURfQVQiLCJsbmdsYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPWR3ZCIsInRhYmxlIjoiRFdEX1NUQVRJT05fTUVSR0UifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSIsIlNUQVRJT05fTkFNRSIsIkJBU0lOX0NPREUiLCJSSVZFUl9OQU1FIiwiTE9OR0lUVURFIiwiTEFUSVRVREUiLCJBRE1JTl9SRUdJT05fQ09ERSIsIlNUQVRVUyIsIkNSRUFURURfQVQiLCJVUERBVEVEX0FUIiwiTE5HTEFUIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoiZHdkIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NDkwNzYwMTc0NzIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueihqOWtl+espuS4suaLvOaOpSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLlrZfmrrXmtL7nlJ/lmagiLCJjb21wb25lbnRUeXBlIjoiMzkiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0OTczOTYyNDc2OCIsInBhcmFtZXRlciI6eyJmaWVsZERlcml2YXRpb25OYW1lIjoibG5nbGF0IiwidGFibGVGaWVsZHMiOlt7ImNvbHVtbk5hbWUiOiJMT05HSVRVREUiLCJvcmRlciI6ImFzYyIsImNhc2VTZW5zaXRpdmUiOmZhbHNlLCJsb2NhbGUiOnRydWUsImNvbGxhdG9yU3RyZW5ndGgiOjAsInByZXNvcnRlZCI6ZmFsc2V9LHsiY29sdW1uTmFtZSI6IkxBVElUVURFIiwib3JkZXIiOiJhc2MiLCJjYXNlU2Vuc2l0aXZlIjpmYWxzZSwibG9jYWxlIjp0cnVlLCJjb2xsYXRvclN0cmVuZ3RoIjowLCJwcmVzb3J0ZWQiOmZhbHNlfV0sImZpZWxkRGVyaXZhdGlvblR5cGUiOiJGSUVMRF9ERVJJVkVfQ09OQ0FUIiwiZGVsaW1pdGVyIjoiLCIsImZpZWxkRGVyaXZhdGlvblN1ZmZpeCI6IiIsImZpZWxkRGVyaXZhdGlvblByZWZpeCI6IiJ9LCJub2RlVmVyc2lvbiI6Mn1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 11:37:12.771', '2025-09-30 15:41:18.321');
INSERT INTO public.t_ds_task_definition VALUES (74, 152656664673600, '单表_ID增量读_追加写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2NDM1MzYxMDg4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudENvbHVtbiI6IiIsImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY0MzY4MDkwMjQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NjQzNDI3MDUyOCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahfSUTlop7ph4/or7tf6L+95Yqg5YaZIiwidGFza1ZlcnNpb24iOjJ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:34:27.701', '2025-09-30 15:57:29.418');
INSERT INTO public.t_ds_task_definition VALUES (73, 152656278439232, '单表_时间增量读_更新写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2MjEwNDM2NDE2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbeyJpbmNyZW1lbnRDb2x1bW4iOiJvYnNfZGF0ZSIsIm9wZXJhdG9yIjoiPiIsInR5cGUiOiIyIiwiZGF0YSI6IiJ9XX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudENvbHVtbiI6IiIsImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIzIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTYyMTE4OTM1NjgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOlsiSUQiLCJTVEFUSU9OX0NPREUiXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6Im9kcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoib2RzIn0sImRiTmFtZSI6Im9kcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9b2RzIiwidGFibGUiOiJPRFNfV1JfV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MywidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NjIwODk2OTAyNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5pe26Ze05aKe6YeP6K+7X+abtOaWsOWGmSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:27:45.333', '2025-09-30 15:56:31.275');
INSERT INTO public.t_ds_task_definition VALUES (75, 152657219431744, '清洗_水位格式标准化-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY5MDE2NzAyMDgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfV0FURVJfTEVWRUxfQ0xFQU5fU1REIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoiZHdkIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTY4OTY0Nzk1NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5riF5rSXX+awtOS9jeWAvOi+ueeVjOiwg+aVtCIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLovazmjaLnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMzEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMDMwMTEyMCIsInBhcmFtZXRlciI6eyJ0YWJsZUZpZWxkcyI6W3sibmFtZSI6IuawtOS9jeWAvOi+ueeVjOiwg+aVtCIsInJ1bGVOYW1lIjoi5pWw5YC86L6555WM6LCD5pW0IiwicnVsZUNvZGUiOiIwMDEiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjMiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwibWluXCI6XCIwXCIsXCJtYXhcIjpcIjEwMFwiLFwiaGFuZGxlVHlwZVwiOlwiMVwiLFwicGFyZW50TmFtZVwiOlwi5byC5bi45YC85L+u5q2j57G7XCJ9IiwiaWQiOiIiLCJwYXJlbnROYW1lIjoi5byC5bi45YC85L+u5q2j57G7IiwicnVsZVR5cGUiOiJXSVRISU5fQk9VTkRBUlkifV0sIndoZXJlIjoiIn0sIm5vZGVWZXJzaW9uIjoyfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:44:11.848', '2025-09-30 16:00:15.143');
INSERT INTO public.t_ds_task_definition VALUES (80, 152658885655872, '清洗_水位异常值处理-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4NDkxMjIzMzYwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTMyNTI2NzIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfV0FURVJfTEVWRUxfQ0xFQU5fT1VUTElFUiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6ImR3ZCJ9LCJub2RlVmVyc2lvbiI6Mn0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU4NDkwMDU0OTc2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3lvILluLjlgLzlpITnkIYiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifX0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTE4NjMxMDQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLlvILluLjlgLzliZTpmaQiLCJydWxlTmFtZSI6IuaVsOWAvOi+ueeVjOiwg+aVtCIsInJ1bGVDb2RlIjoiMDAxIiwic3RhdHVzIjoiMSIsIndoZXJlQ2xhdXNlIjoiIiwiY29sdW1ucyI6WyJXQVRFUl9MRVZFTCJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzIiwicnVsZUNvbmZpZyI6IntcImNvbHVtbnNcIjpbXCJXQVRFUl9MRVZFTFwiXSxcIm1pblwiOlwiMFwiLFwibWF4XCI6XCIxMDBcIixcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIuW8guW4uOWAvOS/ruato+exu1wifSIsImlkIjoiIiwicGFyZW50TmFtZSI6IuW8guW4uOWAvOS/ruato+exuyIsInJ1bGVUeXBlIjoiV0lUSElOX0JPVU5EQVJZIn1dLCJ3aGVyZSI6IiJ9LCJub2RlVmVyc2lvbiI6Mn1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 14:13:36.295', '2025-09-30 16:05:39.349');
INSERT INTO public.t_ds_task_definition VALUES (79, 152658250408256, '清洗_水位缺失补全-2025-09-22', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkwODA5NjY0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjoxMiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IldBVEVSX0xFVkVMIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkzNjU3NDA4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImR3ZCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGF0YXNvdXJjZUlkIjoyLCJjb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPWR3ZCIsInRhYmxlIjoiRFdEX1dBVEVSX0xFVkVMX0NMRUFOX01JU1NJTkcifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NzY4OTUzMjczNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLmuIXmtJdf5rC05L2N57y65aSx6KGl5YWoIiwidGFza1ZlcnNpb24iOjR9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6Iui9rOaNoue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIzMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4MDU4MTY5NjY0IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJuYW1lIjoi5rC05L2N5re75Yqg5Y2V5L2NIiwicnVsZU5hbWUiOiLlrZfmrrXliY3nvIAv5ZCO57yA57uf5LiAIiwicnVsZUNvZGUiOiIwMTAiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwic3RyaW5nVmFsdWVcIjpcIuexs1wiLFwiaGFuZGxlVHlwZVwiOlwiMlwiLFwicGFyZW50TmFtZVwiOlwi5qC85byP5qCH5YeG5YyW57G7XCJ9IiwiaWQiOiIiLCJwYXJlbnROYW1lIjoi5qC85byP5qCH5YeG5YyW57G7IiwicnVsZVR5cGUiOiJBRERfUFJFRklYX1NVRkZJWCJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 14:02:24.009', '2025-09-30 16:03:29.962');
INSERT INTO public.t_ds_task_definition VALUES (89, 152672440882496, 'Hive_水位日统计报表', 2, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"HIVE","sql":"-- ========== 1) 源：MySQL 水位明细 ==========\r\nCREATE TEMPORARY VIEW hyd_water_level_src\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_water_level'',\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 2) 可选维度：站点表（如没有可保留，后续会自动降级为用 station_code 代替名称）==========\r\n-- 期望列：station_id BIGINT, station_code VARCHAR, station_name VARCHAR\r\nCREATE TEMPORARY VIEW hyd_station_dim\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_station'',                       -- 没有就保持这样；后续 LEFT JOIN + COALESCE 兼容\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 3) 目标：Doris 报表（通过 MySQL 协议）==========\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report_jdbc\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://host.docker.internal:9030/dws?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''dws_station_water_day_report'',\r\n  user ''root'',\r\n  password ''InC3tmU4bijT4vkl'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 4) 计算“日平均水位”并写入目标 ==========\r\nWITH base AS (\r\n  SELECT\r\n    wl.station_code,\r\n    COALESCE(wl.obs_date, CAST(wl.obs_time AS DATE)) AS stat_date,\r\n    CAST(wl.water_level_m AS DOUBLE)                  AS water_level_m\r\n  FROM hyd_water_level_src wl\r\n  WHERE wl.water_level_m IS NOT NULL\r\n),\r\nday_avg AS (\r\n  SELECT\r\n    station_code,\r\n    stat_date,\r\n    AVG(water_level_m) AS avg_level_day\r\n  FROM base\r\n  GROUP BY station_code, stat_date\r\n)\r\n\r\nINSERT INTO dws_station_water_day_report_jdbc\r\nSELECT\r\n  -- station_id：有维表用维表，没有则置 NULL\r\n  COALESCE(CAST(s.station_id AS BIGINT), CAST(NULL AS BIGINT))             AS station_id,\r\n  -- station_name：有维表用维表名称，没有则用 station_code 顶上\r\n  COALESCE(CAST(s.station_name AS STRING), CAST(d.station_code AS STRING)) AS station_name,\r\n  -- 写入日平均（保留三位小数），沿用目标表列名 avg_level_year\r\n  CAST(ROUND(d.avg_level_day, 3) AS DECIMAL(10,3))                          AS avg_level_year,\r\n  CAST(d.stat_date AS DATE)                                                 AS stat_date\r\nFROM day_avg d\r\nLEFT JOIN hyd_station_dim s\r\n  ON s.station_code = d.station_code;","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qData123","database":"default","other":{"hive.resultset.use.unique.column.names":false},"port":10000,"connectType":"ORACLE_SERVICE_NAME","host":"hive","type":"HIVE","userName":"admin"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:03:55.894', '2025-09-22 18:06:41.785');
INSERT INTO public.t_ds_task_definition VALUES (91, 152672766952768, 'MySQL_水位日统计', 1, '【MySQL  存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"MYSQL","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"sfdjfFF#s2332","database":"hydrology","other":{},"port":3306,"connectType":"ORACLE_SERVICE_NAME","host":"mysql57","type":"MYSQL","userName":"root"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:13:05.613', '2025-09-22 18:13:05.613');
INSERT INTO public.t_ds_task_definition VALUES (92, 152673322082624, '转化_水位预警等级校验', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition VALUES (93, 152673327374656, '转化_水位列转行', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152642396472640}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition VALUES (94, 152673332505920, '转化_水位行转列', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152648927087936}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition VALUES (95, 152673475740992, '转化_水位预警等级校验', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition VALUES (96, 152673488823616, '多表_更新写', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition VALUES (97, 152673498672448, '清洗_水位格式标准化', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition VALUES (98, 152673502172480, '清洗_水位缺失补全', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition VALUES (99, 152673511663936, '单表_全量读_全量写', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition VALUES (104, 153379496920608, '站点编码非空与格式稽查sjuNHe11759215156767', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":9,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/9","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-30 14:52:36.95', '2025-09-30 14:52:36.95');
INSERT INTO public.t_ds_task_definition VALUES (105, 153379745225248, '水位重复记录稽查M26OVzY1759215413354', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-30 14:56:53.386', '2025-09-30 14:56:53.386');
INSERT INTO public.t_ds_task_definition VALUES (107, 153381999104544, '水位重复记录稽查VsFeLJH1759217760217', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-30 15:36:00.245', '2025-09-30 15:36:00.245');
INSERT INTO public.t_ds_task_definition VALUES (72, 152656044210496, '单表_全量读_全量写-2025-09-22', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMwNjk1NzQ0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU2MzE5NzM2OTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjN9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTYyODc4ODAzMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5YWo6YeP6K+7X+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjo0fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, 0, 0, NULL, -1, -1, '2025-09-22 13:23:43.684', '2025-09-30 15:50:50.085');
INSERT INTO public.t_ds_task_definition VALUES (108, 153383838261792, 'Kingbase_水位日统计', 2, '【Kingbase 存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"KINGBASE","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qdata_dev","database":"test","other":{},"port":54321,"connectType":"ORACLE_SERVICE_NAME","host":"kingbase","type":"KINGBASE","userName":"qdata_dev"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 1, 0, 0, NULL, -1, -1, '2025-09-30 16:08:34.03', '2025-09-30 16:09:14.425');


--
-- Data for Name: t_ds_task_definition_log; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_task_definition_log VALUES (2, 150911519707360, '测试111', 1, '', 141883958809440, 1, 'FLINK', 0, '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;"}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-02 17:26:48.117', -1, -1, '2025-09-02 17:26:48.117', '2025-09-02 17:26:48.117');
INSERT INTO public.t_ds_task_definition_log VALUES (3, 150911519707360, '测试111', 2, '', 141883958809440, 1, 'FLINK', 0, '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;"}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-02 17:31:15.642', -1, -1, '2025-09-02 17:26:48.117', '2025-09-02 17:31:15.642');
INSERT INTO public.t_ds_task_definition_log VALUES (5, 150982603096832, '测试JEoZH8A1756874348816', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-03 12:39:08.862', -1, -1, '2025-09-03 12:39:08.862', '2025-09-03 12:39:08.862');
INSERT INTO public.t_ds_task_definition_log VALUES (6, 150982633305856, '测试9LMXA6X1756874378318', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-03 12:39:38.364', -1, -1, '2025-09-03 12:39:38.364', '2025-09-03 12:39:38.364');
INSERT INTO public.t_ds_task_definition_log VALUES (60, 152416173958464, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 17:35:17.825', -1, -1, '2025-09-19 17:35:17.825', '2025-09-19 17:35:17.825');
INSERT INTO public.t_ds_task_definition_log VALUES (61, 152439162287424, '转化_水位预警等级校验-2025-09-20', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsIndhcm5pbmdfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsIndhcm5pbmdfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:09:32.174', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:09:32.174');
INSERT INTO public.t_ds_task_definition_log VALUES (62, 152439162287424, '转化_水位预警等级校验-2025-09-20', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsIndhcm5pbmdfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6Mn0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsIndhcm5pbmdfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6Mn1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:13:15.02', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:13:15.02');
INSERT INTO public.t_ds_task_definition_log VALUES (63, 152439162287424, '转化_水位预警等级校验-2025-09-20', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6M30sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6M31dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:21:50.37', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:21:50.37');
INSERT INTO public.t_ds_task_definition_log VALUES (64, 152439162287424, '转化_水位预警等级校验-2025-09-20', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6M30sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjo0fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6M31dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:22:23.605', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:22:23.605');
INSERT INTO public.t_ds_task_definition_log VALUES (65, 152439162287424, '转化_水位预警等级校验-2025-09-20', 5, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NH0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjo1fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NH1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:22:31.402', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:22:31.402');
INSERT INTO public.t_ds_task_definition_log VALUES (66, 152439162287424, '转化_水位预警等级校验-2025-09-20', 6, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjo2fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-20 00:23:50.706', -1, -1, '2025-09-20 00:09:32.174', '2025-09-20 00:23:50.706');
INSERT INTO public.t_ds_task_definition_log VALUES (67, 152642396352832, '转化_水位列转行-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQyMzUzOTUyMCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3Il0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfc3RhdGlvbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJzdGF0aW9uX2lkIiwic3RhdGlvbl9uYW1lIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjQxMTMzMjE1MDQwIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iui9rOWMll/msLTkvY3liJfovazooYwiLCJ0YXNrVmVyc2lvbiI6MX0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQxNzE3OTQ1NiIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsIjIwMjVfMDEiLCIyMDI1XzAyIiwiMjAyNV8wMyIsIjIwMjVfMDQiLCIyMDI1XzA1IiwiMjAyNV8wNiIsIjIwMjVfMDciXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi5YiX6L2s6KGMIiwiY29tcG9uZW50VHlwZSI6IjQzIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjE5NTEyOTYiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDEiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTAxIiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wMiIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDIiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzAzIiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wMyIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDQiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA0IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wNSIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDUiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzA2IiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wNiIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDciLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA3IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9XSwia2V5RmllbGQiOiJTVEFUX01PTlRIIiwiZ3JvdXBUYWJsZUZpZWxkcyI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl19LCJub2RlVmVyc2lvbiI6MX0seyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 09:25:52.357', -1, -1, '2025-09-22 09:25:52.357', '2025-09-22 09:25:52.357');
INSERT INTO public.t_ds_task_definition_log VALUES (68, 152642396352832, '转化_水位列转行-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQyMzUzOTUyMCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3Il0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfc3RhdGlvbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJzdGF0aW9uX2lkIiwic3RhdGlvbl9uYW1lIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjQxMTMzMjE1MDQwIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iui9rOWMll/msLTkvY3liJfovazooYwiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQxNzE3OTQ1NiIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsIjIwMjVfMDEiLCIyMDI1XzAyIiwiMjAyNV8wMyIsIjIwMjVfMDQiLCIyMDI1XzA1IiwiMjAyNV8wNiIsIjIwMjVfMDciXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi5YiX6L2s6KGMIiwiY29tcG9uZW50VHlwZSI6IjQzIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjE5NTEyOTYiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDEiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTAxIiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wMiIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDIiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzAzIiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wMyIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDQiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA0IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wNSIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDUiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzA2IiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wNiIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDciLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA3IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9XSwia2V5RmllbGQiOiJTVEFUX01PTlRIIiwiZ3JvdXBUYWJsZUZpZWxkcyI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl19LCJub2RlVmVyc2lvbiI6MX0seyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 09:26:30.26', -1, -1, '2025-09-22 09:25:52.357', '2025-09-22 09:26:30.26');
INSERT INTO public.t_ds_task_definition_log VALUES (69, 152439162287424, '转化_水位预警等级校验-2025-09-20', 7, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI0MTYxNDU0NjM2MTYiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jemihOitpuetiee6p+agoemqjCIsInRhc2tWZXJzaW9uIjo3fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIiwi6aKE6K2m562J57qnIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfc3RhdGlvbl93YXRlcl9sZXZlbF9tb250aF9yZXBvcnQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsImF2Z193YXRlcl9sZXZlbCIsInN0YXRfbW9udGgiLCJ3YXJuaW5nX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6NX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaVsOWAvOiMg+WbtCIsImNvbXBvbmVudFR5cGUiOiI0OSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTYzNzM1ODcyIiwicGFyYW1ldGVyIjp7ImlucHV0RmllbGQiOiJBVkdfV0FURVJfTEVWRUwiLCJ0YWJsZUZpZWxkcyI6W3sibWF4Ijo1MCwibGFiZWwiOiLok53oibLpooToraYifSx7Im1pbiI6NTAsIm1heCI6NjAsImxhYmVsIjoi6buE6Imy6aKE6K2mIn0seyJtaW4iOjYwLCJsYWJlbCI6Iue6ouiJsumihOitpiJ9XSwiZGVmYXVsdFZhbHVlIjoiMC4wMCIsIm91dHB1dEZpZWxkIjoi6aKE6K2m562J57qnIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 09:26:58.945', -1, -1, '2025-09-20 00:09:32.174', '2025-09-22 09:26:58.945');
INSERT INTO public.t_ds_task_definition_log VALUES (70, 152642396352832, '转化_水位列转行-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQyMzUzOTUyMCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3Il0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfc3RhdGlvbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJzdGF0aW9uX2lkIiwic3RhdGlvbl9uYW1lIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjQxMTMzMjE1MDQwIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iui9rOWMll/msLTkvY3liJfovazooYwiLCJ0YXNrVmVyc2lvbiI6M30sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0MTQxNzE3OTQ1NiIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiMjAyNV8wMSIsIjIwMjVfMDIiLCIyMDI1XzAzIiwiMjAyNV8wNCIsIjIwMjVfMDUiLCIyMDI1XzA2IiwiMjAyNV8wNyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fbmFtZSIsIjIwMjVfMDEiLCIyMDI1XzAyIiwiMjAyNV8wMyIsIjIwMjVfMDQiLCIyMDI1XzA1IiwiMjAyNV8wNiIsIjIwMjVfMDciXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi5YiX6L2s6KGMIiwiY29tcG9uZW50VHlwZSI6IjQzIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjE5NTEyOTYiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDEiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTAxIiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wMiIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDIiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzAzIiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wMyIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDQiLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA0IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9LHsiY29sdW1uTmFtZSI6IiIsImNvbHVtblR5cGUiOiJzdHJpbmciLCJ0YXJnZXRDb2x1bW5OYW1lIjoiMjAyNV8wNSIsImRhdGFDb2x1bW4iOiJBVkdfV0FURVJfTEVWRUwiLCJrZXlDb2x1bW5WYWx1ZSI6IjIwMjUtMDUiLCJmb3JtYXQiOiIiLCJsZW5ndGgiOjUwLCJwcmVjaXNpb24iOjAsIm51bGxJZiI6IiIsImFnZ3JlZ2F0aW9uIjoiIn0seyJjb2x1bW5OYW1lIjoiIiwiY29sdW1uVHlwZSI6InN0cmluZyIsInRhcmdldENvbHVtbk5hbWUiOiIyMDI1XzA2IiwiZGF0YUNvbHVtbiI6IkFWR19XQVRFUl9MRVZFTCIsImtleUNvbHVtblZhbHVlIjoiMjAyNS0wNiIsImZvcm1hdCI6IiIsImxlbmd0aCI6NTAsInByZWNpc2lvbiI6MCwibnVsbElmIjoiIiwiYWdncmVnYXRpb24iOiIifSx7ImNvbHVtbk5hbWUiOiIiLCJjb2x1bW5UeXBlIjoic3RyaW5nIiwidGFyZ2V0Q29sdW1uTmFtZSI6IjIwMjVfMDciLCJkYXRhQ29sdW1uIjoiQVZHX1dBVEVSX0xFVkVMIiwia2V5Q29sdW1uVmFsdWUiOiIyMDI1LTA3IiwiZm9ybWF0IjoiIiwibGVuZ3RoIjo1MCwicHJlY2lzaW9uIjowLCJudWxsSWYiOiIiLCJhZ2dyZWdhdGlvbiI6IiJ9XSwia2V5RmllbGQiOiJTVEFUX01PTlRIIiwiZ3JvdXBUYWJsZUZpZWxkcyI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl19LCJub2RlVmVyc2lvbiI6MX0seyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 09:27:17.463', -1, -1, '2025-09-22 09:25:52.357', '2025-09-22 09:27:17.463');
INSERT INTO public.t_ds_task_definition_log VALUES (46, 152150248157120, '232332', 1, '', 141883958809440, 1, 'FLINK', 0, '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;"}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:24:34.34', -1, -1, '2025-09-16 17:24:34.34', '2025-09-16 17:24:34.34');
INSERT INTO public.t_ds_task_definition_log VALUES (47, 152150518559680, '232332JLZSD3A1758014890777', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:28:10.806', -1, -1, '2025-09-16 17:28:10.806', '2025-09-16 17:28:10.806');
INSERT INTO public.t_ds_task_definition_log VALUES (48, 152150534972352, '232332Cjy7WIv1758014906806', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:28:26.839', -1, -1, '2025-09-16 17:28:26.839', '2025-09-16 17:28:26.839');
INSERT INTO public.t_ds_task_definition_log VALUES (49, 152150622975936, '2323_user_20250916172917OMdTTDk1758014992747', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:29:52.755', -1, -1, '2025-09-16 17:29:52.755', '2025-09-16 17:29:52.755');
INSERT INTO public.t_ds_task_definition_log VALUES (50, 152150699760576, '1212-2025-09-16', 1, '', 141883958809440, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgxMTkxMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6InRlc3QiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsIm5hbWUiXSwiZGJUeXBlIjoiTXlTcWwiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlcl9jb3B5MSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgyOTgzMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IlRBWWoyVWR5cytFR2o5aGpJS3JiS1E9PSIsImRibmFtZSI6InRlc3QifSwiZGJOYW1lIjoidGVzdCIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6InRvcGRlbW8uY24iLCJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInBvcnQiOjMzMDYsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL3RvcGRlbW8uY246MzMwNi90ZXN0P3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJ1c2VyX2NvcHkxIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwibmFtZSJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyMTUwNjc5NDgxMjgwIiwicHJvamVjdENvZGUiOjE0MTg4Mzk1ODgwOTQ0MCwibmFtZSI6IjEyMTIiLCJ0YXNrVmVyc2lvbiI6MX0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4MTE5MTM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vdG9wZGVtby5jbjozMzA2L3Rlc3Q/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6InVzZXJfY29weTEifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4Mjk4MzM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJyb290IiwicGFzc3dvcmQiOiJUQVlqMlVkeXMrRUdqOWhqSUtyYktRPT0iLCJkYm5hbWUiOiJ0ZXN0In0sImRiTmFtZSI6InRlc3QiLCJkYlR5cGUiOiJNeVNxbCIsImhvc3QiOiJ0b3BkZW1vLmNuIiwicGFzc3dvcmQiOiJ3YW5nbWluZzExMTQiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwiZGJOYW1lIjoidGVzdCIsImRhdGFzb3VyY2VJZCI6MSwiY29sdW1uIjpbImlkIiwibmFtZSJdLCJkYlR5cGUiOiJNeVNxbCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlcl9jb3B5MSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsIm5hbWUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOltdfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:31:07.742', -1, -1, '2025-09-16 17:31:07.742', '2025-09-16 17:31:07.742');
INSERT INTO public.t_ds_task_definition_log VALUES (51, 152150699760576, '1212-2025-09-16', 2, '', 141883958809440, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgxMTkxMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6InRlc3QiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsIm5hbWUiXSwiZGJUeXBlIjoiTXlTcWwiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlcl9jb3B5MSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNDE4ODM5NTg4MDk0NDAsIm5vZGVDb2RlIjoiMTUyMTUwNjgyOTgzMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoid2FuZ21pbmcxMTE0Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IlRBWWoyVWR5cytFR2o5aGpJS3JiS1E9PSIsImRibmFtZSI6InRlc3QifSwiZGJOYW1lIjoidGVzdCIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6InRvcGRlbW8uY24iLCJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInBvcnQiOjMzMDYsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL3RvcGRlbW8uY246MzMwNi90ZXN0P3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJ1c2VyIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwibmFtZSJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyMTUwNjc5NDgxMjgwIiwicHJvamVjdENvZGUiOjE0MTg4Mzk1ODgwOTQ0MCwibmFtZSI6IjEyMTIiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4MTE5MTM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJ0ZXN0IiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJuYW1lIl0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vdG9wZGVtby5jbjozMzA2L3Rlc3Q/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6InVzZXJfY29weTEifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTQxODgzOTU4ODA5NDQwLCJub2RlQ29kZSI6IjE1MjE1MDY4Mjk4MzM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IndhbmdtaW5nMTExNCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJyb290IiwicGFzc3dvcmQiOiJUQVlqMlVkeXMrRUdqOWhqSUtyYktRPT0iLCJkYm5hbWUiOiJ0ZXN0In0sImRiTmFtZSI6InRlc3QiLCJkYlR5cGUiOiJNeVNxbCIsImhvc3QiOiJ0b3BkZW1vLmNuIiwicGFzc3dvcmQiOiJ3YW5nbWluZzExMTQiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwiZGJOYW1lIjoidGVzdCIsImRhdGFzb3VyY2VJZCI6MSwiY29sdW1uIjpbImlkIiwibmFtZSJdLCJkYlR5cGUiOiJNeVNxbCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly90b3BkZW1vLmNuOjMzMDYvdGVzdD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoidXNlciJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsIm5hbWUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfV0sInRyYW5zaXRpb24iOltdfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-16 17:31:49.207', -1, -1, '2025-09-16 17:31:07.742', '2025-09-16 17:31:49.207');
INSERT INTO public.t_ds_task_definition_log VALUES (52, 152413380820288, '工程监管数据发现uVOWKbY1758271591783', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 16:46:32.742', -1, -1, '2025-09-19 16:46:32.742', '2025-09-19 16:46:32.742');
INSERT INTO public.t_ds_task_definition_log VALUES (53, 152413553931584, '工程监管数据发现wSAxLkg1758271769213', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 16:49:29.242', -1, -1, '2025-09-19 16:49:29.242', '2025-09-19 16:49:29.242');
INSERT INTO public.t_ds_task_definition_log VALUES (54, 152413555441984, '水文监测数据发现Rg0XxWU1758271770687', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":2,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/2","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 16:49:30.698', -1, -1, '2025-09-19 16:49:30.698', '2025-09-19 16:49:30.698');
INSERT INTO public.t_ds_task_definition_log VALUES (55, 152413557107008, '水质监测数据发现OemKK9R1758271772314', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/3","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 16:49:32.327', -1, -1, '2025-09-19 16:49:32.327', '2025-09-19 16:49:32.327');
INSERT INTO public.t_ds_task_definition_log VALUES (56, 152413579804992, '水资源数据发现xAxpna41758271796919', 1, '', 134799536571008, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/4","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 16:49:56.929', -1, -1, '2025-09-19 16:49:56.929', '2025-09-19 16:49:56.929');
INSERT INTO public.t_ds_task_definition_log VALUES (59, 152416165498176, '水资源测站点数据（原始库）_ods_wr_station_20250919173418HSto32K1758274506657', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-19 17:35:06.691', -1, -1, '2025-09-19 17:35:06.691', '2025-09-19 17:35:06.691');
INSERT INTO public.t_ds_task_definition_log VALUES (73, 152648927077696, '转化_水位行转列-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3IiwiMjAyNV8wOCIsIjIwMjVfMDkiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUX1dJREUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiYXZnX3dhdGVyX2xldmVsIiwic3RhdF9tb250aCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0In0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbInN0YXRpb25faWQiLCJzdGF0aW9uX25hbWUiLCJhdmdfd2F0ZXJfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NDI3ODAzMTA4NDgiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jeihjOi9rOWIlyIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3IiwiMjAyNV8wOCIsIjIwMjVfMDkiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUX1dJREUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiYXZnX3dhdGVyX2xldmVsIiwic3RhdF9tb250aCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0In0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbInN0YXRpb25faWQiLCJzdGF0aW9uX25hbWUiLCJhdmdfd2F0ZXJfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuihjOi9rOWIlyIsImNvbXBvbmVudFR5cGUiOiI0MiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3ODgyODA0NTQ0IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiMjAyNV8wMSIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTAxIiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzAyIiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDIiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDMiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wMyIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn0seyJjb2x1bW5OYW1lIjoiMjAyNV8wNCIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTA0IiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzA1IiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDUiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDYiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wNiIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn0seyJjb2x1bW5OYW1lIjoiMjAyNV8wNyIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTA3IiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzA4IiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDgiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDkiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wOSIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn1dLCJrZXlGaWVsZCI6InN0YXRfbW9udGgiLCJncm91cEZpZWxkcyI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl19LCJub2RlVmVyc2lvbiI6MX1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 11:19:31.097', -1, -1, '2025-09-22 11:19:31.097', '2025-09-22 11:19:31.097');
INSERT INTO public.t_ds_task_definition_log VALUES (74, 152649930990912, '转化_站点表字符串拼接-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ5NzM4NDY4NjcyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0OTc0MTA5ODMwNCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCIsImxuZ2xhdCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fbWVyZ2UifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fY29kZSIsInN0YXRpb25fbmFtZSIsImJhc2luX2NvZGUiLCJyaXZlcl9uYW1lIiwibG9uZ2l0dWRlIiwibGF0aXR1ZGUiLCJhZG1pbl9yZWdpb25fY29kZSIsInN0YXR1cyIsImNyZWF0ZWRfYXQiLCJ1cGRhdGVkX2F0IiwibG5nbGF0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NDkwNzYwMTc0NzIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueihqOWtl+espuS4suaLvOaOpSIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ5NzM4NDY4NjcyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0OTc0MTA5ODMwNCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCIsImxuZ2xhdCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fbWVyZ2UifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fY29kZSIsInN0YXRpb25fbmFtZSIsImJhc2luX2NvZGUiLCJyaXZlcl9uYW1lIiwibG9uZ2l0dWRlIiwibGF0aXR1ZGUiLCJhZG1pbl9yZWdpb25fY29kZSIsInN0YXR1cyIsImNyZWF0ZWRfYXQiLCJ1cGRhdGVkX2F0IiwibG5nbGF0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuWtl+autea0vueUn+WZqCIsImNvbXBvbmVudFR5cGUiOiIzOSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ5NzM5NjI0NzY4IiwicGFyYW1ldGVyIjp7ImZpZWxkRGVyaXZhdGlvbk5hbWUiOiJsbmdsYXQiLCJ0YWJsZUZpZWxkcyI6W3siY29sdW1uTmFtZSI6IkxPTkdJVFVERSIsIm9yZGVyIjoiYXNjIiwiY2FzZVNlbnNpdGl2ZSI6ZmFsc2UsImxvY2FsZSI6dHJ1ZSwiY29sbGF0b3JTdHJlbmd0aCI6MCwicHJlc29ydGVkIjpmYWxzZX0seyJjb2x1bW5OYW1lIjoiTEFUSVRVREUiLCJvcmRlciI6ImFzYyIsImNhc2VTZW5zaXRpdmUiOmZhbHNlLCJsb2NhbGUiOnRydWUsImNvbGxhdG9yU3RyZW5ndGgiOjAsInByZXNvcnRlZCI6ZmFsc2V9XSwiZmllbGREZXJpdmF0aW9uVHlwZSI6IkZJRUxEX0RFUklWRV9DT05DQVQiLCJkZWxpbWl0ZXIiOiIsIiwiZmllbGREZXJpdmF0aW9uU3VmZml4IjoiIiwiZmllbGREZXJpdmF0aW9uUHJlZml4IjoiIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 11:37:12.771', -1, -1, '2025-09-22 11:37:12.771', '2025-09-22 11:37:12.771');
INSERT INTO public.t_ds_task_definition_log VALUES (79, 152653935271232, '转化_站点数据去重-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTEzNjI4NDgwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1MDExNjYyODgwMCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoib2RzIn0sImRiTmFtZSI6Im9kcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6Im9kcyIsImRhdGFzb3VyY2VJZCI6MSwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL29kcz91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoib2RzX3N0YXRpb24ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fY29kZSIsInN0YXRpb25fbmFtZSIsImJhc2luX2NvZGUiLCJyaXZlcl9uYW1lIiwibG9uZ2l0dWRlIiwibGF0aXR1ZGUiLCJhZG1pbl9yZWdpb25fY29kZSIsInN0YXR1cyIsImNyZWF0ZWRfYXQiLCJ1cGRhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTAxMTE4NjgyMjQiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueaVsOaNruWOu+mHjSIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTEzNjI4NDgwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1MDExNjYyODgwMCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoib2RzIn0sImRiTmFtZSI6Im9kcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6Im9kcyIsImRhdGFzb3VyY2VJZCI6MSwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL29kcz91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoib2RzX3N0YXRpb24ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsic3RhdGlvbl9pZCIsInN0YXRpb25fY29kZSIsInN0YXRpb25fbmFtZSIsImJhc2luX2NvZGUiLCJyaXZlcl9uYW1lIiwibG9uZ2l0dWRlIiwibGF0aXR1ZGUiLCJhZG1pbl9yZWdpb25fY29kZSIsInN0YXR1cyIsImNyZWF0ZWRfYXQiLCJ1cGRhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuWOu+mZpOmHjeWkjeiusOW9lSIsImNvbXBvbmVudFR5cGUiOiI0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTE1MTQyOTc2IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9OQU1FIiwiY29sdW1uVHlwZSI6Ik5VTUJFUiIsImlnbm9yZUNhc2UiOjEsInNvdXJjZSI6IuWOu+mZpOmHjeWkjeiusOW9lSJ9XX0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 12:46:47.632', -1, -1, '2025-09-22 12:46:47.632', '2025-09-22 12:46:47.632');
INSERT INTO public.t_ds_task_definition_log VALUES (80, 152654997784896, '多表_更新写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc2MzIxMDg4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7InF1ZXJ5U3FsIjoiU0VMRUNUXG4gIHMuXCJCQVNJTl9DT0RFXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIGJhc2luX2NvZGUsXG4gIHIuXCJTVEFUX01PTlRIXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIHN0YXRfbW9udGgsXG4gIENPVU5UKERJU1RJTkNUIHMuXCJTVEFUSU9OX0lEXCIpICAgICAgICAgICAgICAgICAgIEFTIHN0YXRpb25fY250LCAgICAgXG4gIFJPVU5EKEFWRyhUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIGF2Z19sZXZlbCwgXG4gIFJPVU5EKE1BWChUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1heF9sZXZlbCxcbiAgUk9VTkQoTUlOKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWluX2xldmVsXG5GUk9NIFwiV0FURVJfVFBcIi5cIlNUQVRJT05cIiBzXG5KT0lOIFwiV0FURVJfVFBcIi5cIlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUXCIgclxuICBPTiByLlwiU1RBVElPTl9JRFwiID0gcy5cIlNUQVRJT05fSURcIlxuLS0g5aaC6ZyA6L+H5ruk5pyI5Lu977yM5Y+v5Yqg77yaV0hFUkUgci5cIlNUQVRfTU9OVEhcIiBCRVRXRUVOICcyMDI1LTAxJyBBTkQgJzIwMjUtMTInXG5HUk9VUCBCWVxuICBzLlwiQkFTSU5fQ09ERVwiLFxuICByLlwiU1RBVF9NT05USFwiXG5PUkRFUiBCWVxuICByLlwiU1RBVF9NT05USFwiLFxuICBzLlwiQkFTSU5fQ09ERVwiIiwiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoxLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTQ0NzgxMzY2NDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfYmFzaW5fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGVfdXBkYXRlIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwiaGRmcyI6eyJ1cmwiOiJoZGZzOi8vbmFtZW5vZGU6ODAyMCJ9LCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NDQ3MzM4MDE2MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf5pu05paw5YaZIiwidGFza1ZlcnNpb24iOjF9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn0sInJlZGlzIjp7ImRhdGFiYXNlIjowLCJob3N0IjoicmVkaXMiLCJwYXNzd29yZCI6Iko5OCVGSEYjOWhAZTg4aDlmcmU5IiwicG9ydCI6NjM3OX19LCJkYXRhTGluZWFnZVRhc2tMaXN0IjpbeyJub2RlTmFtZSI6IuihqOi+k+WFpee7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTQ0NzYzMjEwODgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYXRhc291cmNlSWQiOjEyLCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsicXVlcnlTcWwiOiJTRUxFQ1RcbiAgcy5cIkJBU0lOX0NPREVcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgYmFzaW5fY29kZSxcbiAgci5cIlNUQVRfTU9OVEhcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgc3RhdF9tb250aCxcbiAgQ09VTlQoRElTVElOQ1Qgcy5cIlNUQVRJT05fSURcIikgICAgICAgICAgICAgICAgICAgQVMgc3RhdGlvbl9jbnQsICAgICBcbiAgUk9VTkQoQVZHKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgYXZnX2xldmVsLCBcbiAgUk9VTkQoTUFYKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWF4X2xldmVsLFxuICBST1VORChNSU4oVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtaW5fbGV2ZWxcbkZST00gXCJXQVRFUl9UUFwiLlwiU1RBVElPTlwiIHNcbkpPSU4gXCJXQVRFUl9UUFwiLlwiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlRcIiByXG4gIE9OIHIuXCJTVEFUSU9OX0lEXCIgPSBzLlwiU1RBVElPTl9JRFwiXG4tLSDlpoLpnIDov4fmu6TmnIjku73vvIzlj6/liqDvvJpXSEVSRSByLlwiU1RBVF9NT05USFwiIEJFVFdFRU4gJzIwMjUtMDEnIEFORCAnMjAyNS0xMidcbkdST1VQIEJZXG4gIHMuXCJCQVNJTl9DT0RFXCIsXG4gIHIuXCJTVEFUX01PTlRIXCJcbk9SREVSIEJZXG4gIHIuXCJTVEFUX01PTlRIXCIsXG4gIHMuXCJCQVNJTl9DT0RFXCIiLCJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NDQ3ODEzNjY0MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9hZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6ImFkc19iYXNpbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZV91cGRhdGUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9XSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:05:39.183', -1, -1, '2025-09-22 13:05:39.183', '2025-09-22 13:05:39.183');
INSERT INTO public.t_ds_task_definition_log VALUES (81, 152648927077696, '转化_水位行转列-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3IiwiMjAyNV8wOCIsIjIwMjVfMDkiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUX1dJREUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiYXZnX3dhdGVyX2xldmVsIiwic3RhdF9tb250aCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0In0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbInN0YXRpb25faWQiLCJzdGF0aW9uX25hbWUiLCJhdmdfd2F0ZXJfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NDI3ODAzMTA4NDgiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+awtOS9jeihjOi9rOWIlyIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCIyMDI1XzAxIiwiMjAyNV8wMiIsIjIwMjVfMDMiLCIyMDI1XzA0IiwiMjAyNV8wNSIsIjIwMjVfMDYiLCIyMDI1XzA3IiwiMjAyNV8wOCIsIjIwMjVfMDkiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUX1dJREUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiYXZnX3dhdGVyX2xldmVsIiwic3RhdF9tb250aCJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3N0YXRpb25fd2F0ZXJfbW9udGhfcmVwb3J0In0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbInN0YXRpb25faWQiLCJzdGF0aW9uX25hbWUiLCJhdmdfd2F0ZXJfbGV2ZWwiLCJzdGF0X21vbnRoIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuihjOi9rOWIlyIsImNvbXBvbmVudFR5cGUiOiI0MiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3ODgyODA0NTQ0IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiMjAyNV8wMSIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTAxIiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzAyIiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDIiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDMiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wMyIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn0seyJjb2x1bW5OYW1lIjoiMjAyNV8wNCIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTA0IiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzA1IiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDUiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDYiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wNiIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn0seyJjb2x1bW5OYW1lIjoiMjAyNV8wNyIsImNvbHVtblR5cGUiOiIiLCJjb2x1bW5LZXkiOiIyMDI1LTA3IiwiY29sdW1uVmFsdWUiOiJhdmdfd2F0ZXJfbGV2ZWwifSx7ImNvbHVtbk5hbWUiOiIyMDI1XzA4IiwiY29sdW1uVHlwZSI6IiIsImNvbHVtbktleSI6IjIwMjUtMDgiLCJjb2x1bW5WYWx1ZSI6ImF2Z193YXRlcl9sZXZlbCJ9LHsiY29sdW1uTmFtZSI6IjIwMjVfMDkiLCJjb2x1bW5UeXBlIjoiIiwiY29sdW1uS2V5IjoiMjAyNS0wOSIsImNvbHVtblZhbHVlIjoiYXZnX3dhdGVyX2xldmVsIn1dLCJrZXlGaWVsZCI6InN0YXRfbW9udGgiLCJncm91cEZpZWxkcyI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl19LCJub2RlVmVyc2lvbiI6MX1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:06:01.959', -1, -1, '2025-09-22 11:19:31.097', '2025-09-22 13:06:01.959');
INSERT INTO public.t_ds_task_definition_log VALUES (82, 152654997784896, '多表_更新写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc2MzIxMDg4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7InF1ZXJ5U3FsIjoiU0VMRUNUXG4gIHMuXCJCQVNJTl9DT0RFXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIGJhc2luX2NvZGUsXG4gIHIuXCJTVEFUX01PTlRIXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIHN0YXRfbW9udGgsXG4gIENPVU5UKERJU1RJTkNUIHMuXCJTVEFUSU9OX0lEXCIpICAgICAgICAgICAgICAgICAgIEFTIHN0YXRpb25fY250LCAgICAgXG4gIFJPVU5EKEFWRyhUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIGF2Z19sZXZlbCwgXG4gIFJPVU5EKE1BWChUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1heF9sZXZlbCxcbiAgUk9VTkQoTUlOKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWluX2xldmVsXG5GUk9NIFwiV0FURVJfVFBcIi5cIlNUQVRJT05cIiBzXG5KT0lOIFwiV0FURVJfVFBcIi5cIlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUXCIgclxuICBPTiByLlwiU1RBVElPTl9JRFwiID0gcy5cIlNUQVRJT05fSURcIlxuLS0g5aaC6ZyA6L+H5ruk5pyI5Lu977yM5Y+v5Yqg77yaV0hFUkUgci5cIlNUQVRfTU9OVEhcIiBCRVRXRUVOICcyMDI1LTAxJyBBTkQgJzIwMjUtMTInXG5HUk9VUCBCWVxuICBzLlwiQkFTSU5fQ09ERVwiLFxuICByLlwiU1RBVF9NT05USFwiXG5PUkRFUiBCWVxuICByLlwiU1RBVF9NT05USFwiLFxuICBzLlwiQkFTSU5fQ09ERVwiIiwiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoxLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTQ0NzgxMzY2NDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfYmFzaW5fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGVfdXBkYXRlIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwiaGRmcyI6eyJ1cmwiOiJoZGZzOi8vbmFtZW5vZGU6ODAyMCJ9LCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NDQ3MzM4MDE2MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf5pu05paw5YaZIiwidGFza1ZlcnNpb24iOjJ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn0sInJlZGlzIjp7ImRhdGFiYXNlIjowLCJob3N0IjoicmVkaXMiLCJwYXNzd29yZCI6Iko5OCVGSEYjOWhAZTg4aDlmcmU5IiwicG9ydCI6NjM3OX19LCJkYXRhTGluZWFnZVRhc2tMaXN0IjpbeyJub2RlTmFtZSI6IuihqOi+k+WFpee7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTQ0NzYzMjEwODgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYXRhc291cmNlSWQiOjEyLCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsicXVlcnlTcWwiOiJTRUxFQ1RcbiAgcy5cIkJBU0lOX0NPREVcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgYmFzaW5fY29kZSxcbiAgci5cIlNUQVRfTU9OVEhcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgc3RhdF9tb250aCxcbiAgQ09VTlQoRElTVElOQ1Qgcy5cIlNUQVRJT05fSURcIikgICAgICAgICAgICAgICAgICAgQVMgc3RhdGlvbl9jbnQsICAgICBcbiAgUk9VTkQoQVZHKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgYXZnX2xldmVsLCBcbiAgUk9VTkQoTUFYKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWF4X2xldmVsLFxuICBST1VORChNSU4oVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtaW5fbGV2ZWxcbkZST00gXCJXQVRFUl9UUFwiLlwiU1RBVElPTlwiIHNcbkpPSU4gXCJXQVRFUl9UUFwiLlwiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlRcIiByXG4gIE9OIHIuXCJTVEFUSU9OX0lEXCIgPSBzLlwiU1RBVElPTl9JRFwiXG4tLSDlpoLpnIDov4fmu6TmnIjku73vvIzlj6/liqDvvJpXSEVSRSByLlwiU1RBVF9NT05USFwiIEJFVFdFRU4gJzIwMjUtMDEnIEFORCAnMjAyNS0xMidcbkdST1VQIEJZXG4gIHMuXCJCQVNJTl9DT0RFXCIsXG4gIHIuXCJTVEFUX01PTlRIXCJcbk9SREVSIEJZXG4gIHIuXCJTVEFUX01PTlRIXCIsXG4gIHMuXCJCQVNJTl9DT0RFXCIiLCJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NDQ3ODEzNjY0MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9hZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6ImFkc19iYXNpbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZV91cGRhdGUifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9XSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:10:04.147', -1, -1, '2025-09-22 13:05:39.183', '2025-09-22 13:10:04.147');
INSERT INTO public.t_ds_task_definition_log VALUES (83, 152655297790272, '多表_全量写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjYzNjUwMTEyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7InF1ZXJ5U3FsIjoiU0VMRUNUXG4gIHMuXCJCQVNJTl9DT0RFXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIGJhc2luX2NvZGUsXG4gIHIuXCJTVEFUX01PTlRIXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIHN0YXRfbW9udGgsXG4gIENPVU5UKERJU1RJTkNUIHMuXCJTVEFUSU9OX0lEXCIpICAgICAgICAgICAgICAgICAgIEFTIHN0YXRpb25fY250LCAgICAgXG4gIFJPVU5EKEFWRyhUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIGF2Z19sZXZlbCwgXG4gIFJPVU5EKE1BWChUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1heF9sZXZlbCxcbiAgUk9VTkQoTUlOKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWluX2xldmVsXG5GUk9NIFwiV0FURVJfVFBcIi5cIlNUQVRJT05cIiBzXG5KT0lOIFwiV0FURVJfVFBcIi5cIlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUXCIgclxuICBPTiByLlwiU1RBVElPTl9JRFwiID0gcy5cIlNUQVRJT05fSURcIlxuLS0g5aaC6ZyA6L+H5ruk5pyI5Lu977yM5Y+v5Yqg77yaV0hFUkUgci5cIlNUQVRfTU9OVEhcIiBCRVRXRUVOICcyMDI1LTAxJyBBTkQgJzIwMjUtMTInXG5HUk9VUCBCWVxuICBzLlwiQkFTSU5fQ09ERVwiLFxuICByLlwiU1RBVF9NT05USFwiXG5PUkRFUiBCWVxuICByLlwiU1RBVF9NT05USFwiLFxuICBzLlwiQkFTSU5fQ09ERVwiIiwiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoxLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTUyNjYzODQxOTIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfYmFzaW5fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGVfZnVsbCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTUyMzg2NzQ3NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5aSa6KGoX+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjYzNjUwMTEyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7InF1ZXJ5U3FsIjoiU0VMRUNUXG4gIHMuXCJCQVNJTl9DT0RFXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIGJhc2luX2NvZGUsXG4gIHIuXCJTVEFUX01PTlRIXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIHN0YXRfbW9udGgsXG4gIENPVU5UKERJU1RJTkNUIHMuXCJTVEFUSU9OX0lEXCIpICAgICAgICAgICAgICAgICAgIEFTIHN0YXRpb25fY250LCAgICAgXG4gIFJPVU5EKEFWRyhUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIGF2Z19sZXZlbCwgXG4gIFJPVU5EKE1BWChUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1heF9sZXZlbCxcbiAgUk9VTkQoTUlOKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWluX2xldmVsXG5GUk9NIFwiV0FURVJfVFBcIi5cIlNUQVRJT05cIiBzXG5KT0lOIFwiV0FURVJfVFBcIi5cIlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUXCIgclxuICBPTiByLlwiU1RBVElPTl9JRFwiID0gcy5cIlNUQVRJT05fSURcIlxuLS0g5aaC6ZyA6L+H5ruk5pyI5Lu977yM5Y+v5Yqg77yaV0hFUkUgci5cIlNUQVRfTU9OVEhcIiBCRVRXRUVOICcyMDI1LTAxJyBBTkQgJzIwMjUtMTInXG5HUk9VUCBCWVxuICBzLlwiQkFTSU5fQ09ERVwiLFxuICByLlwiU1RBVF9NT05USFwiXG5PUkRFUiBCWVxuICByLlwiU1RBVF9NT05USFwiLFxuICBzLlwiQkFTSU5fQ09ERVwiIiwiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoxLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTUyNjYzODQxOTIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfYmFzaW5fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGVfZnVsbCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:10:50.235', -1, -1, '2025-09-22 13:10:50.235', '2025-09-22 13:10:50.235');
INSERT INTO public.t_ds_task_definition_log VALUES (84, 152655501865280, '多表_追加写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NDI2MjMyNjQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04Iiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7InF1ZXJ5U3FsIjoiU0VMRUNUXG4gIHMuXCJCQVNJTl9DT0RFXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIGJhc2luX2NvZGUsXG4gIHIuXCJTVEFUX01PTlRIXCIgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIEFTIHN0YXRfbW9udGgsXG4gIENPVU5UKERJU1RJTkNUIHMuXCJTVEFUSU9OX0lEXCIpICAgICAgICAgICAgICAgICAgIEFTIHN0YXRpb25fY250LCAgICAgXG4gIFJPVU5EKEFWRyhUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIGF2Z19sZXZlbCwgXG4gIFJPVU5EKE1BWChUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1heF9sZXZlbCxcbiAgUk9VTkQoTUlOKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWluX2xldmVsXG5GUk9NIFwiV0FURVJfVFBcIi5cIlNUQVRJT05cIiBzXG5KT0lOIFwiV0FURVJfVFBcIi5cIlNUQVRJT05fV0FURVJfTU9OVEhfUkVQT1JUXCIgclxuICBPTiByLlwiU1RBVElPTl9JRFwiID0gcy5cIlNUQVRJT05fSURcIlxuLS0g5aaC6ZyA6L+H5ruk5pyI5Lu977yM5Y+v5Yqg77yaV0hFUkUgci5cIlNUQVRfTU9OVEhcIiBCRVRXRUVOICcyMDI1LTAxJyBBTkQgJzIwMjUtMTInXG5HUk9VUCBCWVxuICBzLlwiQkFTSU5fQ09ERVwiLFxuICByLlwiU1RBVF9NT05USFwiXG5PUkRFUiBCWVxuICByLlwiU1RBVF9NT05USFwiLFxuICBzLlwiQkFTSU5fQ09ERVwiIiwiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoxLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU0MjgyMjAyMjQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJiYXNpbl9jb2RlIiwic3RhdF9tb250aCIsInN0YXRpb25fY250IiwiYXZnX2xldmVsIiwibWF4X2xldmVsIiwibWluX2xldmVsIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvYWRzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJhZHNfYmFzaW5fd2F0ZXJfbW9udGhfcmVwb3J0X3dpZGVfYXBwZW5kIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwiaGRmcyI6eyJ1cmwiOiJoZGZzOi8vbmFtZW5vZGU6ODAyMCJ9LCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTQyNDg1MzMxMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf6L+95Yqg5YaZIiwidGFza1ZlcnNpb24iOjF9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn0sInJlZGlzIjp7ImRhdGFiYXNlIjowLCJob3N0IjoicmVkaXMiLCJwYXNzd29yZCI6Iko5OCVGSEYjOWhAZTg4aDlmcmU5IiwicG9ydCI6NjM3OX19LCJkYXRhTGluZWFnZVRhc2tMaXN0IjpbeyJub2RlTmFtZSI6IuihqOi+k+WFpee7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU0MjYyMzI2NDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYXRhc291cmNlSWQiOjEyLCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsicXVlcnlTcWwiOiJTRUxFQ1RcbiAgcy5cIkJBU0lOX0NPREVcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgYmFzaW5fY29kZSxcbiAgci5cIlNUQVRfTU9OVEhcIiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgQVMgc3RhdF9tb250aCxcbiAgQ09VTlQoRElTVElOQ1Qgcy5cIlNUQVRJT05fSURcIikgICAgICAgICAgICAgICAgICAgQVMgc3RhdGlvbl9jbnQsICAgICBcbiAgUk9VTkQoQVZHKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgYXZnX2xldmVsLCBcbiAgUk9VTkQoTUFYKFRPX05VTUJFUihOVUxMSUYoci5cIkFWR19XQVRFUl9MRVZFTFwiLCAnJykpKSwgMykgQVMgbWF4X2xldmVsLFxuICBST1VORChNSU4oVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtaW5fbGV2ZWxcbkZST00gXCJXQVRFUl9UUFwiLlwiU1RBVElPTlwiIHNcbkpPSU4gXCJXQVRFUl9UUFwiLlwiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlRcIiByXG4gIE9OIHIuXCJTVEFUSU9OX0lEXCIgPSBzLlwiU1RBVElPTl9JRFwiXG4tLSDlpoLpnIDov4fmu6TmnIjku73vvIzlj6/liqDvvJpXSEVSRSByLlwiU1RBVF9NT05USFwiIEJFVFdFRU4gJzIwMjUtMDEnIEFORCAnMjAyNS0xMidcbkdST1VQIEJZXG4gIHMuXCJCQVNJTl9DT0RFXCIsXG4gIHIuXCJTVEFUX01PTlRIXCJcbk9SREVSIEJZXG4gIHIuXCJTVEFUX01PTlRIXCIsXG4gIHMuXCJCQVNJTl9DT0RFXCIiLCJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NTQyODIyMDIyNCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImFkcyIsImRhdGFzb3VyY2VJZCI6NCwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9hZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6ImFkc19iYXNpbl93YXRlcl9tb250aF9yZXBvcnRfd2lkZV9hcHBlbmQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9XSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:14:24.026', -1, -1, '2025-09-22 13:14:24.026', '2025-09-22 13:14:24.026');
INSERT INTO public.t_ds_task_definition_log VALUES (85, 152656044210496, '单表_全量读_全量写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMwNjk1NzQ0IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoic2ZkamZGRiNzMjMzMiIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJoeWRyb2xvZ3kiLCJkYXRhc291cmNlSWQiOjExLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU2MzE5NzM2OTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9vZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Im9kc19oeWRfd2F0ZXJfbGV2ZWwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTU2Mjg3ODgwMzIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5Y2V6KGoX+WFqOmHj+ivu1/lhajph4/lhpkiLCJ0YXNrVmVyc2lvbiI6MX0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NTYzMDY5NTc0NCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGJOYW1lIjoiaHlkcm9sb2d5IiwiZGF0YXNvdXJjZUlkIjoxMSwiY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwic2Vuc29yX2lkIiwib2JzX3RpbWUiLCJvYnNfZGF0ZSIsIndhdGVyX2xldmVsX20iLCJxdWFsaXR5X2NvZGUiLCJzb3VyY2UiLCJ0cmFjZV9pZCIsImV4dF9qc29uIiwiY3JlYXRlZF9hdCJdLCJkYlR5cGUiOiJNeVNxbCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL215c3FsNTc6MzMwNi9oeWRyb2xvZ3k/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Imh5ZF93YXRlcl9sZXZlbCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0seyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMxOTczNjk2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJyb290IiwicGFzc3dvcmQiOiJCN0w0djZsMHlNaXV5OW4rNkgyL3NRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRG9yaXMiLCJob3N0IjoiaG9zdC5kb2NrZXIuaW50ZXJuYWwiLCJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJwb3J0Ijo5MDMwLCJ1c2VybmFtZSI6InJvb3QifSwiZGJOYW1lIjoib2RzIiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvb2RzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJvZHNfaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwic2Vuc29yX2lkIiwib2JzX3RpbWUiLCJvYnNfZGF0ZSIsIndhdGVyX2xldmVsX20iLCJxdWFsaXR5X2NvZGUiLCJzb3VyY2UiLCJ0cmFjZV9pZCIsImV4dF9qc29uIiwiY3JlYXRlZF9hdCJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9XSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:23:43.684', -1, -1, '2025-09-22 13:23:43.684', '2025-09-22 13:23:43.684');
INSERT INTO public.t_ds_task_definition_log VALUES (86, 152656278439232, '单表_时间增量读_更新写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2MjEwNDM2NDE2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoic2ZkamZGRiNzMjMzMiIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W3siZGF0YSI6IjIwMjUtMDUtMDEiLCJpbmNyZW1lbnRDb2x1bW4iOiJvYnNfdGltZSIsIm9wZXJhdG9yIjoiPiIsInR5cGUiOiIxIn1dfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50Q29sdW1uIjoiIiwiaW5jcmVtZW50U3RhcnQiOiIifSwiZGJOYW1lIjoiaHlkcm9sb2d5IiwiZGF0YXNvdXJjZUlkIjoxMSwiY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwic2Vuc29yX2lkIiwib2JzX3RpbWUiLCJvYnNfZGF0ZSIsIndhdGVyX2xldmVsX20iLCJxdWFsaXR5X2NvZGUiLCJzb3VyY2UiLCJ0cmFjZV9pZCIsImV4dF9qc29uIiwiY3JlYXRlZF9hdCJdLCJkYlR5cGUiOiJNeVNxbCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL215c3FsNTc6MzMwNi9oeWRyb2xvZ3k/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Imh5ZF93YXRlcl9sZXZlbCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMyIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2MjExODkzNTY4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJyb290IiwicGFzc3dvcmQiOiJCN0w0djZsMHlNaXV5OW4rNkgyL3NRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRG9yaXMiLCJob3N0IjoiaG9zdC5kb2NrZXIuaW50ZXJuYWwiLCJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJwb3J0Ijo5MDMwLCJ1c2VybmFtZSI6InJvb3QifSwiZGJOYW1lIjoib2RzIiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvb2RzP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJvZHNfaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwic2Vuc29yX2lkIiwib2JzX3RpbWUiLCJvYnNfZGF0ZSIsIndhdGVyX2xldmVsX20iLCJxdWFsaXR5X2NvZGUiLCJzb3VyY2UiLCJ0cmFjZV9pZCIsImV4dF9qc29uIiwiY3JlYXRlZF9hdCJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU2MjA4OTY5MDI0IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6IuWNleihqF/ml7bpl7Tlop7ph4/or7tf5pu05paw5YaZIiwidGFza1ZlcnNpb24iOjF9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn0sInJlZGlzIjp7ImRhdGFiYXNlIjowLCJob3N0IjoicmVkaXMiLCJwYXNzd29yZCI6Iko5OCVGSEYjOWhAZTg4aDlmcmU5IiwicG9ydCI6NjM3OX19LCJkYXRhTGluZWFnZVRhc2tMaXN0IjpbeyJub2RlTmFtZSI6IuihqOi+k+WFpee7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTYyMTA0MzY0MTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbeyJkYXRhIjoiMjAyNS0wNS0wMSIsImluY3JlbWVudENvbHVtbiI6Im9ic190aW1lIiwib3BlcmF0b3IiOiI+IiwidHlwZSI6IjEifV19LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRDb2x1bW4iOiIiLCJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJoeWRyb2xvZ3kiLCJkYXRhc291cmNlSWQiOjExLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIzIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTYyMTE4OTM1NjgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9vZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Im9kc19oeWRfd2F0ZXJfbGV2ZWwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:27:45.333', -1, -1, '2025-09-22 13:27:45.333', '2025-09-22 13:27:45.333');
INSERT INTO public.t_ds_task_definition_log VALUES (87, 152656664673600, '单表_ID增量读_追加写-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2NDM1MzYxMDg4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoic2ZkamZGRiNzMjMzMiIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRDb2x1bW4iOiIiLCJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJoeWRyb2xvZ3kiLCJkYXRhc291cmNlSWQiOjExLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY0MzY4MDkwMjQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9vZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Im9kc19oeWRfd2F0ZXJfbGV2ZWwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTY0MzQyNzA1MjgiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5Y2V6KGoX0lE5aKe6YeP6K+7X+i/veWKoOWGmSIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2NDM1MzYxMDg4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoic2ZkamZGRiNzMjMzMiIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRDb2x1bW4iOiIiLCJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJoeWRyb2xvZ3kiLCJkYXRhc291cmNlSWQiOjExLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sImRiVHlwZSI6Ik15U3FsIiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY0MzY4MDkwMjQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRG9yaXMiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vaG9zdC5kb2NrZXIuaW50ZXJuYWw6OTAzMC9vZHM/dXNlVW5pY29kZT10cnVlJmNoYXJhY3RlckVuY29kaW5nPXV0Zi04Jnplcm9EYXRlVGltZUJlaGF2aW9yPWNvbnZlcnRUb051bGwmdXNlU1NMPWZhbHNlJnNlcnZlclRpbWV6b25lPUdNVCUyQjgiLCJ0YWJsZSI6Im9kc19oeWRfd2F0ZXJfbGV2ZWwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSIsInF1YWxpdHlfY29kZSIsInNvdXJjZSIsInRyYWNlX2lkIiwiZXh0X2pzb24iLCJjcmVhdGVkX2F0Il0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:34:27.701', -1, -1, '2025-09-22 13:34:27.701', '2025-09-22 13:34:27.701');
INSERT INTO public.t_ds_task_definition_log VALUES (88, 152657219431744, '清洗_水位格式标准化-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMTY3MDIwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fc3RkIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwib2JzX3RpbWUiLCJ3YXRlcl9sZXZlbCIsInF1YWxpdHlfZmxhZyIsInRzIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTY4OTY0Nzk1NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5riF5rSXX+awtOS9jeagvOW8j+agh+WHhuWMliIsInRhc2tWZXJzaW9uIjoxfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMTY3MDIwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fc3RkIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwib2JzX3RpbWUiLCJ3YXRlcl9sZXZlbCIsInF1YWxpdHlfZmxhZyIsInRzIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6Iui9rOaNoue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIzMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2OTAwMzAxMTIwIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJuYW1lIjoi5rC05L2N5YC85L+d55WZMuS9jeWwj+aVsCIsInJ1bGVOYW1lIjoi5bCP5pWw5L2N57uf5LiAIiwicnVsZUNvZGUiOiIwMDgiLCJzdGF0dXMiOiIwIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwic3RyaW5nVmFsdWVcIjpcIjJcIixcInBhcmVudE5hbWVcIjpcIuagvOW8j+agh+WHhuWMluexu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJGSVhfVE9fUFJFQ0lTSU9OIiwicGFyZW50TmFtZSI6IuagvOW8j+agh+WHhuWMluexuyJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:44:11.848', -1, -1, '2025-09-22 13:44:11.848', '2025-09-22 13:44:11.848');
INSERT INTO public.t_ds_task_definition_log VALUES (89, 152657219431744, '清洗_水位格式标准化-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMTY3MDIwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fc3RkIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwib2JzX3RpbWUiLCJ3YXRlcl9sZXZlbCIsInF1YWxpdHlfZmxhZyIsInRzIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsImhkZnMiOnsidXJsIjoiaGRmczovL25hbWVub2RlOjgwMjAifSwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTY4OTY0Nzk1NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5riF5rSXX+awtOS9jeagvOW8j+agh+WHhuWMliIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9LCJyZWRpcyI6eyJkYXRhYmFzZSI6MCwiaG9zdCI6InJlZGlzIiwicGFzc3dvcmQiOiJKOTglRkhGIzloQGU4OGg5ZnJlOSIsInBvcnQiOjYzNzl9fSwiZGF0YUxpbmVhZ2VUYXNrTGlzdCI6W3sibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LHsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMTY3MDIwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fc3RkIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbImlkIiwic3RhdGlvbl9jb2RlIiwib2JzX3RpbWUiLCJ3YXRlcl9sZXZlbCIsInF1YWxpdHlfZmxhZyIsInRzIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoicm9vdCJ9LCJub2RlVmVyc2lvbiI6MX1dLCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6Iui9rOaNoue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIzMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2OTAwMzAxMTIwIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJuYW1lIjoi5rC05L2N5YC85L+d55WZMuS9jeWwj+aVsCIsInJ1bGVOYW1lIjoi5bCP5pWw5L2N57uf5LiAIiwicnVsZUNvZGUiOiIwMDgiLCJzdGF0dXMiOiIwIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwic3RyaW5nVmFsdWVcIjpcIjJcIixcInBhcmVudE5hbWVcIjpcIuagvOW8j+agh+WHhuWMluexu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJGSVhfVE9fUFJFQ0lTSU9OIiwicGFyZW50TmFtZSI6IuagvOW8j+agh+WHhuWMluexuyJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:44:26.179', -1, -1, '2025-09-22 13:44:11.848', '2025-09-22 13:44:26.179');
INSERT INTO public.t_ds_task_definition_log VALUES (92, 152657963461952, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/3","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 13:57:17.76', -1, -1, '2025-09-22 13:57:17.76', '2025-09-22 13:57:17.76');
INSERT INTO public.t_ds_task_definition_log VALUES (93, 152658250408256, '清洗_水位缺失补全-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkwODA5NjY0IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NzY5MzY1NzQwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fbWlzc2luZyJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsIm9ic190aW1lIiwid2F0ZXJfbGV2ZWwiLCJxdWFsaXR5X2ZsYWciLCJ0cyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU3Njg5NTMyNzM2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3nvLrlpLHooaXlhagiLCJ0YXNrVmVyc2lvbiI6MX0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NzY5MDgwOTY2NCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTc2OTM2NTc0MDgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3dhdGVyX2xldmVsX2NsZWFuX21pc3NpbmcifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJvYnNfdGltZSIsIndhdGVyX2xldmVsIiwicXVhbGl0eV9mbGFnIiwidHMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTgwNTgxNjk2NjQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLnvLrlpLHlgLzooaXlhajkuLogMC4wMCIsInJ1bGVOYW1lIjoi5q2j5YiZ6KGo6L6+5byP5pu/5o2iIiwicnVsZUNvZGUiOiIwMTEiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwicmVnZXhcIjpcIl5cXFxccyokXCIsXCJyZXBsYWNlbWVudFwiOlwiMC4wMFwiLFwicGFyZW50TmFtZVwiOlwi5qC85byP5qCH5YeG5YyW57G7XCJ9IiwiaWQiOiIiLCJydWxlVHlwZSI6IlNJTVBMRV9SRVBMQUNFIiwicGFyZW50TmFtZSI6IuagvOW8j+agh+WHhuWMluexuyJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 14:02:24.009', -1, -1, '2025-09-22 14:02:24.009', '2025-09-22 14:02:24.009');
INSERT INTO public.t_ds_task_definition_log VALUES (94, 152658250408256, '清洗_水位缺失补全-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkwODA5NjY0IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NzY5MzY1NzQwOCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fbWlzc2luZyJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsIm9ic190aW1lIiwid2F0ZXJfbGV2ZWwiLCJxdWFsaXR5X2ZsYWciLCJ0cyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU3Njg5NTMyNzM2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3nvLrlpLHooaXlhagiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NzY5MDgwOTY2NCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTc2OTM2NTc0MDgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3dhdGVyX2xldmVsX2NsZWFuX21pc3NpbmcifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJvYnNfdGltZSIsIndhdGVyX2xldmVsIiwicXVhbGl0eV9mbGFnIiwidHMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTgwNTgxNjk2NjQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLnvLrlpLHlgLzooaXlhajkuLogMC4wMCIsInJ1bGVOYW1lIjoi5q2j5YiZ6KGo6L6+5byP5pu/5o2iIiwicnVsZUNvZGUiOiIwMTEiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwicmVnZXhcIjpcIl5cXFxccyokXCIsXCJyZXBsYWNlbWVudFwiOlwiMC4wMFwiLFwicGFyZW50TmFtZVwiOlwi5qC85byP5qCH5YeG5YyW57G7XCJ9IiwiaWQiOiIiLCJydWxlVHlwZSI6IlNJTVBMRV9SRVBMQUNFIiwicGFyZW50TmFtZSI6IuagvOW8j+agh+WHhuWMluexuyJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjF9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 14:02:36.722', -1, -1, '2025-09-22 14:02:24.009', '2025-09-22 14:02:36.722');
INSERT INTO public.t_ds_task_definition_log VALUES (95, 152658885655872, '清洗_水位异常值处理-2025-09-22', 1, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4NDkxMjIzMzYwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGF0YXNvdXJjZUlkIjoxMiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJXQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjF9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1ODU5MzI1MjY3MiIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6IkluQzN0bVU0YmlqVDR2a2wiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiQjdMNHY2bDB5TWl1eTluKzZIMi9zUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRvcmlzIiwiaG9zdCI6Imhvc3QuZG9ja2VyLmludGVybmFsIiwicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwicG9ydCI6OTAzMCwidXNlcm5hbWUiOiJyb290In0sImRiTmFtZSI6ImR3ZCIsImRhdGFzb3VyY2VJZCI6MiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRvcmlzIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpteXNxbDovL2hvc3QuZG9ja2VyLmludGVybmFsOjkwMzAvZHdkP3VzZVVuaWNvZGU9dHJ1ZSZjaGFyYWN0ZXJFbmNvZGluZz11dGYtOCZ6ZXJvRGF0ZVRpbWVCZWhhdmlvcj1jb252ZXJ0VG9OdWxsJnVzZVNTTD1mYWxzZSZzZXJ2ZXJUaW1lem9uZT1HTVQlMkI4IiwidGFibGUiOiJkd2Rfd2F0ZXJfbGV2ZWxfY2xlYW5fb3V0bGllciJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsIm9ic190aW1lIiwid2F0ZXJfbGV2ZWwiLCJxdWFsaXR5X2ZsYWciLCJ0cyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6InJvb3QifSwibm9kZVZlcnNpb24iOjF9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJoZGZzIjp7InVybCI6ImhkZnM6Ly9uYW1lbm9kZTo4MDIwIn0sInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU4NDkwMDU0OTc2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3lvILluLjlgLzlpITnkIYiLCJ0YXNrVmVyc2lvbiI6MX0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifSwicmVkaXMiOnsiZGF0YWJhc2UiOjAsImhvc3QiOiJyZWRpcyIsInBhc3N3b3JkIjoiSjk4JUZIRiM5aEBlODhoOWZyZTkiLCJwb3J0Ijo2Mzc5fX0sImRhdGFMaW5lYWdlVGFza0xpc3QiOlt7Im5vZGVOYW1lIjoi6KGo6L6T5YWl57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1ODQ5MTIyMzM2MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRhdGFzb3VyY2VJZCI6MTIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoxfSx7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTMyNTI2NzIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJJbkMzdG1VNGJpalQ0dmtsIiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6InJvb3QiLCJwYXNzd29yZCI6IkI3TDR2NmwweU1pdXk5bis2SDIvc1E9PSIsImRibmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYlR5cGUiOiJEb3JpcyIsImhvc3QiOiJob3N0LmRvY2tlci5pbnRlcm5hbCIsInBhc3N3b3JkIjoiSW5DM3RtVTRiaWpUNHZrbCIsInBvcnQiOjkwMzAsInVzZXJuYW1lIjoicm9vdCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJEb3JpcyIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6bXlzcWw6Ly9ob3N0LmRvY2tlci5pbnRlcm5hbDo5MDMwL2R3ZD91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiZHdkX3dhdGVyX2xldmVsX2NsZWFuX291dGxpZXIifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJvYnNfdGltZSIsIndhdGVyX2xldmVsIiwicXVhbGl0eV9mbGFnIiwidHMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoxfV0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTE4NjMxMDQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLlvILluLjlgLzliZTpmaQiLCJydWxlTmFtZSI6IuaVsOWAvOi+ueeVjOiwg+aVtCIsInJ1bGVDb2RlIjoiMDAxIiwic3RhdHVzIjoiMSIsIndoZXJlQ2xhdXNlIjoiIiwiY29sdW1ucyI6WyJXQVRFUl9MRVZFTCJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzIiwicnVsZUNvbmZpZyI6IntcImNvbHVtbnNcIjpbXCJXQVRFUl9MRVZFTFwiXSxcIm1pblwiOlwiMFwiLFwibWF4XCI6XCIxMDBcIixcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIuW8guW4uOWAvOS/ruato+exu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJXSVRISU5fQk9VTkRBUlkiLCJwYXJlbnROYW1lIjoi5byC5bi45YC85L+u5q2j57G7In1dLCJ3aGVyZSI6IiJ9LCJub2RlVmVyc2lvbiI6MX1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 14:13:36.295', -1, -1, '2025-09-22 14:13:36.295', '2025-09-22 14:13:36.295');
INSERT INTO public.t_ds_task_definition_log VALUES (104, 152662937492800, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 15:24:10.631', -1, -1, '2025-09-22 15:24:10.631', '2025-09-22 15:24:10.631');
INSERT INTO public.t_ds_task_definition_log VALUES (167, 152673488823616, '多表_更新写', 3, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:38.432', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.432');
INSERT INTO public.t_ds_task_definition_log VALUES (168, 152673498672448, '清洗_水位格式标准化', 3, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:38.432', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.432');
INSERT INTO public.t_ds_task_definition_log VALUES (169, 152673502172480, '清洗_水位缺失补全', 3, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:38.432', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.432');
INSERT INTO public.t_ds_task_definition_log VALUES (170, 152673511663936, '单表_全量读_全量写', 3, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:38.432', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.432');
INSERT INTO public.t_ds_task_definition_log VALUES (119, 152665378277696, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 16:07:25.877', -1, -1, '2025-09-22 16:07:25.877', '2025-09-22 16:07:25.877');
INSERT INTO public.t_ds_task_definition_log VALUES (171, 152673475740992, '转化_水位预警等级校验', 4, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:46.542', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.542');
INSERT INTO public.t_ds_task_definition_log VALUES (172, 152673488823616, '多表_更新写', 4, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:46.542', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.542');
INSERT INTO public.t_ds_task_definition_log VALUES (173, 152673498672448, '清洗_水位格式标准化', 4, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:46.542', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.542');
INSERT INTO public.t_ds_task_definition_log VALUES (174, 152673502172480, '清洗_水位缺失补全', 4, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:46.542', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.542');
INSERT INTO public.t_ds_task_definition_log VALUES (175, 152673511663936, '单表_全量读_全量写', 4, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:46.542', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:46.542');
INSERT INTO public.t_ds_task_definition_log VALUES (176, 152673475740992, '转化_水位预警等级校验', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:52.567', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition_log VALUES (177, 152673488823616, '多表_更新写', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:52.567', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition_log VALUES (178, 152673498672448, '清洗_水位格式标准化', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:52.567', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition_log VALUES (179, 152673502172480, '清洗_水位缺失补全', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:52.567', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition_log VALUES (180, 152673511663936, '单表_全量读_全量写', 5, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:52.567', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:52.567');
INSERT INTO public.t_ds_task_definition_log VALUES (149, 152672766952768, 'MySQL_水位日统计', 1, '【MySQL  存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"MYSQL","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"sfdjfFF#s2332","database":"hydrology","other":{},"port":3306,"connectType":"ORACLE_SERVICE_NAME","host":"mysql57","type":"MYSQL","userName":"root"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:13:05.613', -1, -1, '2025-09-22 18:13:05.613', '2025-09-22 18:13:05.613');
INSERT INTO public.t_ds_task_definition_log VALUES (150, 152673322082624, '转化_水位预警等级校验', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:24.225', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:24.225');
INSERT INTO public.t_ds_task_definition_log VALUES (151, 152673327374656, '转化_水位列转行', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152642396472640}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:24.225', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:24.225');
INSERT INTO public.t_ds_task_definition_log VALUES (145, 152672440882496, 'Hive_水位日统计报表', 1, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"HIVE","sql":"-- ========== 1) 源：MySQL 水位明细 ==========\r\nCREATE TEMPORARY VIEW hyd_water_level_src\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_water_level'',\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 2) 可选维度：站点表（如没有可保留，后续会自动降级为用 station_code 代替名称）==========\r\n-- 期望列：station_id BIGINT, station_code VARCHAR, station_name VARCHAR\r\nCREATE TEMPORARY VIEW hyd_station_dim\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_station'',                       -- 没有就保持这样；后续 LEFT JOIN + COALESCE 兼容\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 3) 目标：Doris 报表（通过 MySQL 协议）==========\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report_jdbc\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://host.docker.internal:9030/dws?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''dws_station_water_day_report'',\r\n  user ''root'',\r\n  password ''InC3tmU4bijT4vkl'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 4) 计算“日平均水位”并写入目标 ==========\r\nWITH base AS (\r\n  SELECT\r\n    wl.station_code,\r\n    COALESCE(wl.obs_date, CAST(wl.obs_time AS DATE)) AS stat_date,\r\n    CAST(wl.water_level_m AS DOUBLE)                  AS water_level_m\r\n  FROM hyd_water_level_src wl\r\n  WHERE wl.water_level_m IS NOT NULL\r\n),\r\nday_avg AS (\r\n  SELECT\r\n    station_code,\r\n    stat_date,\r\n    AVG(water_level_m) AS avg_level_day\r\n  FROM base\r\n  GROUP BY station_code, stat_date\r\n)\r\n\r\nINSERT INTO dws_station_water_day_report_jdbc\r\nSELECT\r\n  -- station_id：有维表用维表，没有则置 NULL\r\n  COALESCE(CAST(s.station_id AS BIGINT), CAST(NULL AS BIGINT))             AS station_id,\r\n  -- station_name：有维表用维表名称，没有则用 station_code 顶上\r\n  COALESCE(CAST(s.station_name AS STRING), CAST(d.station_code AS STRING)) AS station_name,\r\n  -- 写入日平均（保留三位小数），沿用目标表列名 avg_level_year\r\n  CAST(ROUND(d.avg_level_day, 3) AS DECIMAL(10,3))                          AS avg_level_year,\r\n  CAST(d.stat_date AS DATE)                                                 AS stat_date\r\nFROM day_avg d\r\nLEFT JOIN hyd_station_dim s\r\n  ON s.station_code = d.station_code;","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qData123","database":"default","other":{"hive.resultset.use.unique.column.names":false},"port":10000,"connectType":"ORACLE_SERVICE_NAME","host":"hive","type":"HIVE","userName":"admin"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:03:55.894', -1, -1, '2025-09-22 18:03:55.894', '2025-09-22 18:03:55.894');
INSERT INTO public.t_ds_task_definition_log VALUES (146, 152672440882496, 'Hive_水位日统计报表', 2, '【离线计算】 查询水位实时数据，生成每日统计报表    （Hive 非完全部署，任务无法运行）', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"HIVE","sql":"-- ========== 1) 源：MySQL 水位明细 ==========\r\nCREATE TEMPORARY VIEW hyd_water_level_src\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_water_level'',\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 2) 可选维度：站点表（如没有可保留，后续会自动降级为用 station_code 代替名称）==========\r\n-- 期望列：station_id BIGINT, station_code VARCHAR, station_name VARCHAR\r\nCREATE TEMPORARY VIEW hyd_station_dim\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://mysql57:3306/hydrology?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''hyd_station'',                       -- 没有就保持这样；后续 LEFT JOIN + COALESCE 兼容\r\n  user ''root'',\r\n  password ''sfdjfFF#s2332'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 3) 目标：Doris 报表（通过 MySQL 协议）==========\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report_jdbc\r\nUSING jdbc\r\nOPTIONS (\r\n  url ''jdbc:mysql://host.docker.internal:9030/dws?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  dbtable ''dws_station_water_day_report'',\r\n  user ''root'',\r\n  password ''InC3tmU4bijT4vkl'',\r\n  driver ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- ========== 4) 计算“日平均水位”并写入目标 ==========\r\nWITH base AS (\r\n  SELECT\r\n    wl.station_code,\r\n    COALESCE(wl.obs_date, CAST(wl.obs_time AS DATE)) AS stat_date,\r\n    CAST(wl.water_level_m AS DOUBLE)                  AS water_level_m\r\n  FROM hyd_water_level_src wl\r\n  WHERE wl.water_level_m IS NOT NULL\r\n),\r\nday_avg AS (\r\n  SELECT\r\n    station_code,\r\n    stat_date,\r\n    AVG(water_level_m) AS avg_level_day\r\n  FROM base\r\n  GROUP BY station_code, stat_date\r\n)\r\n\r\nINSERT INTO dws_station_water_day_report_jdbc\r\nSELECT\r\n  -- station_id：有维表用维表，没有则置 NULL\r\n  COALESCE(CAST(s.station_id AS BIGINT), CAST(NULL AS BIGINT))             AS station_id,\r\n  -- station_name：有维表用维表名称，没有则用 station_code 顶上\r\n  COALESCE(CAST(s.station_name AS STRING), CAST(d.station_code AS STRING)) AS station_name,\r\n  -- 写入日平均（保留三位小数），沿用目标表列名 avg_level_year\r\n  CAST(ROUND(d.avg_level_day, 3) AS DECIMAL(10,3))                          AS avg_level_year,\r\n  CAST(d.stat_date AS DATE)                                                 AS stat_date\r\nFROM day_avg d\r\nLEFT JOIN hyd_station_dim s\r\n  ON s.station_code = d.station_code;","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qData123","database":"default","other":{"hive.resultset.use.unique.column.names":false},"port":10000,"connectType":"ORACLE_SERVICE_NAME","host":"hive","type":"HIVE","userName":"admin"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:06:41.785', -1, -1, '2025-09-22 18:03:55.894', '2025-09-22 18:06:41.785');
INSERT INTO public.t_ds_task_definition_log VALUES (152, 152673332505920, '转化_水位行转列', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152648927087936}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:24.225', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:24.225');
INSERT INTO public.t_ds_task_definition_log VALUES (153, 152673322082624, '转化_水位预警等级校验', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:42.892', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition_log VALUES (154, 152673327374656, '转化_水位列转行', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152642396472640}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:42.892', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition_log VALUES (155, 152673332505920, '转化_水位行转列', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152648927087936}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:19:42.892', -1, -1, '2025-09-22 18:19:24.225', '2025-09-22 18:19:42.892');
INSERT INTO public.t_ds_task_definition_log VALUES (156, 152673475740992, '转化_水位预警等级校验', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:22:19.723', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_task_definition_log VALUES (157, 152673488823616, '多表_更新写', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:22:19.723', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_task_definition_log VALUES (158, 152673498672448, '清洗_水位格式标准化', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:22:19.723', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_task_definition_log VALUES (159, 152673502172480, '清洗_水位缺失补全', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:22:19.723', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_task_definition_log VALUES (160, 152673511663936, '单表_全量读_全量写', 1, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:22:19.723', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:22:19.723');
INSERT INTO public.t_ds_task_definition_log VALUES (161, 152673475740992, '转化_水位预警等级校验', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:31.253', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.253');
INSERT INTO public.t_ds_task_definition_log VALUES (162, 152673488823616, '多表_更新写', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152654997830976}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:31.253', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.253');
INSERT INTO public.t_ds_task_definition_log VALUES (163, 152673498672448, '清洗_水位格式标准化', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152657219441984}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:31.253', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.253');
INSERT INTO public.t_ds_task_definition_log VALUES (164, 152673502172480, '清洗_水位缺失补全', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152658250428736}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:31.253', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.253');
INSERT INTO public.t_ds_task_definition_log VALUES (165, 152673511663936, '单表_全量读_全量写', 2, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152656044221760}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:31.253', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:31.253');
INSERT INTO public.t_ds_task_definition_log VALUES (166, 152673475740992, '转化_水位预警等级校验', 3, '', 152317790975712, 1, 'SUB_PROCESS', 0, '{"localParams":[],"resourceList":[],"processDefinitionCode":152439162752320}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-22 18:23:38.432', -1, -1, '2025-09-22 18:22:19.723', '2025-09-22 18:23:38.432');
INSERT INTO public.t_ds_task_definition_log VALUES (207, 152656044210496, '单表_全量读_全量写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMwNjk1NzQ0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU2MzE5NzM2OTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTYyODc4ODAzMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5YWo6YeP6K+7X+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:50:31.831', -1, -1, '2025-09-22 13:23:43.684', '2025-09-30 15:50:31.831');
INSERT INTO public.t_ds_task_definition_log VALUES (208, 152656044210496, '单表_全量读_全量写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMwNjk1NzQ0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU2MzE5NzM2OTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTYyODc4ODAzMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5YWo6YeP6K+7X+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:50:38.651', -1, -1, '2025-09-22 13:23:43.684', '2025-09-30 15:50:38.651');
INSERT INTO public.t_ds_task_definition_log VALUES (209, 152656044210496, '单表_全量读_全量写-2025-09-22', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NjMwNjk1NzQ0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTU2MzE5NzM2OTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjN9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTYyODc4ODAzMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5YWo6YeP6K+7X+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjo0fSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:50:50.085', -1, -1, '2025-09-22 13:23:43.684', '2025-09-30 15:50:50.085');
INSERT INTO public.t_ds_task_definition_log VALUES (210, 152656278439232, '单表_时间增量读_更新写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2MjEwNDM2NDE2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbeyJpbmNyZW1lbnRDb2x1bW4iOiJvYnNfZGF0ZSIsIm9wZXJhdG9yIjoiPiIsInR5cGUiOiIyIiwiZGF0YSI6IiJ9XX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudENvbHVtbiI6IiIsImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIzIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTYyMTE4OTM1NjgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOlsiSUQiLCJTVEFUSU9OX0NPREUiXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6Im9kcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoib2RzIn0sImRiTmFtZSI6Im9kcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9b2RzIiwidGFibGUiOiJPRFNfV1JfV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MywidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NjIwODk2OTAyNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5pe26Ze05aKe6YeP6K+7X+abtOaWsOWGmSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:55:28.748', -1, -1, '2025-09-22 13:27:45.333', '2025-09-30 15:55:28.748');
INSERT INTO public.t_ds_task_definition_log VALUES (211, 152656278439232, '单表_时间增量读_更新写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2MjEwNDM2NDE2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbeyJpbmNyZW1lbnRDb2x1bW4iOiJvYnNfZGF0ZSIsIm9wZXJhdG9yIjoiPiIsInR5cGUiOiIyIiwiZGF0YSI6IiJ9XX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudENvbHVtbiI6IiIsImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIzIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTYyMTE4OTM1NjgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOlsiSUQiLCJTVEFUSU9OX0NPREUiXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6Im9kcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoib2RzIn0sImRiTmFtZSI6Im9kcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGF0YXNvdXJjZUlkIjoxLCJjb2x1bW4iOlsiaWQiLCJzdGF0aW9uX2NvZGUiLCJzZW5zb3JfaWQiLCJvYnNfdGltZSIsIm9ic19kYXRlIiwid2F0ZXJfbGV2ZWxfbSJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9b2RzIiwidGFibGUiOiJPRFNfV1JfV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MywidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NjIwODk2OTAyNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahf5pe26Ze05aKe6YeP6K+7X+abtOaWsOWGmSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:56:31.275', -1, -1, '2025-09-22 13:27:45.333', '2025-09-30 15:56:31.275');
INSERT INTO public.t_ds_task_definition_log VALUES (212, 152656664673600, '单表_ID增量读_追加写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2NDM1MzYxMDg4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6Imh5ZHJvbG9neSIsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiTXlTcWwiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoicm9vdCIsInBhc3N3b3JkIjoiTkVkVU5rckpPOFliWkJJaTlNMm5PZz09IiwiZGJuYW1lIjoiaHlkcm9sb2d5In0sImRiTmFtZSI6Imh5ZHJvbG9neSIsImRiVHlwZSI6Ik15U3FsIiwiaG9zdCI6Im15c3FsNTciLCJwYXNzd29yZCI6InNmZGpmRkYjczIzMzIiLCJwb3J0IjozMzA2LCJ1c2VybmFtZSI6InJvb3QifSwicGFzc3dvcmQiOiJzZmRqZkZGI3MyMzMyIiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudENvbHVtbiI6IiIsImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6Nywid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOm15c3FsOi8vbXlzcWw1NzozMzA2L2h5ZHJvbG9neT91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTgmemVyb0RhdGVUaW1lQmVoYXZpb3I9Y29udmVydFRvTnVsbCZ1c2VTU0w9ZmFsc2Umc2VydmVyVGltZXpvbmU9R01UJTJCOCIsInRhYmxlIjoiaHlkX3dhdGVyX2xldmVsIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJyb290In0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY0MzY4MDkwMjQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJpZCIsInN0YXRpb25fY29kZSIsInNlbnNvcl9pZCIsIm9ic190aW1lIiwib2JzX2RhdGUiLCJ3YXRlcl9sZXZlbF9tIiwicXVhbGl0eV9jb2RlIiwic291cmNlIiwidHJhY2VfaWQiLCJleHRfanNvbiIsImNyZWF0ZWRfYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX0hZRF9XQVRFUl9MRVZFTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIlNFTlNPUl9JRCIsIk9CU19USU1FIiwiT0JTX0RBVEUiLCJXQVRFUl9MRVZFTF9NIiwiUVVBTElUWV9DT0RFIiwiU09VUkNFIiwiVFJBQ0VfSUQiLCJFWFRfSlNPTiIsIkNSRUFURURfQVQiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJvZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NjQzNDI3MDUyOCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLljZXooahfSUTlop7ph4/or7tf6L+95Yqg5YaZIiwidGFza1ZlcnNpb24iOjJ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:57:29.418', -1, -1, '2025-09-22 13:34:27.701', '2025-09-30 15:57:29.418');
INSERT INTO public.t_ds_task_definition_log VALUES (213, 152657219431744, '清洗_水位格式标准化-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU2ODk5MTAzMDQwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTY5MDE2NzAyMDgiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfV0FURVJfTEVWRUxfQ0xFQU5fU1REIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoiZHdkIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTY4OTY0Nzk1NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5riF5rSXX+awtOS9jeWAvOi+ueeVjOiwg+aVtCIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLovazmjaLnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMzEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY1NjkwMDMwMTEyMCIsInBhcmFtZXRlciI6eyJ0YWJsZUZpZWxkcyI6W3sibmFtZSI6IuawtOS9jeWAvOi+ueeVjOiwg+aVtCIsInJ1bGVOYW1lIjoi5pWw5YC86L6555WM6LCD5pW0IiwicnVsZUNvZGUiOiIwMDEiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjMiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwibWluXCI6XCIwXCIsXCJtYXhcIjpcIjEwMFwiLFwiaGFuZGxlVHlwZVwiOlwiMVwiLFwicGFyZW50TmFtZVwiOlwi5byC5bi45YC85L+u5q2j57G7XCJ9IiwiaWQiOiIiLCJwYXJlbnROYW1lIjoi5byC5bi45YC85L+u5q2j57G7IiwicnVsZVR5cGUiOiJXSVRISU5fQk9VTkRBUlkifV0sIndoZXJlIjoiIn0sIm5vZGVWZXJzaW9uIjoyfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 16:00:15.143', -1, -1, '2025-09-22 13:44:11.848', '2025-09-30 16:00:15.143');
INSERT INTO public.t_ds_task_definition_log VALUES (187, 152439162287424, '转化_水位预警等级校验-2025-09-20', 8, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPVdBVEVSX1RQIiwidGFibGUiOiJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwicmVhZE1vZGVUeXBlIjoiMSIsInVzZXJuYW1lIjoiV0FURVJfVFAifSwibm9kZVZlcnNpb24iOjJ9LCJ3cml0ZXIiOnsibm9kZU5hbWUiOiLooajovpPlh7rnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiOTEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjQzODk2NjEzNDA4MCIsInBhcmFtZXRlciI6eyJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInNlbGVjdGVkQ29sdW1ucyI6W10sIndyaXRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJhZHMiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfU1RBVElPTl9XQVRFUl9MRVZFTF9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjZ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjQxNjE0NTQ2MzYxNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6aKE6K2m562J57qn5qCh6aqMIiwidGFza1ZlcnNpb24iOjh9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 14:34:19.936', -1, -1, '2025-09-20 00:09:32.174', '2025-09-30 14:34:19.936');
INSERT INTO public.t_ds_task_definition_log VALUES (188, 153379496920608, '站点编码非空与格式稽查sjuNHe11759215156767', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":9,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/9","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 14:52:36.95', -1, -1, '2025-09-30 14:52:36.95', '2025-09-30 14:52:36.95');
INSERT INTO public.t_ds_task_definition_log VALUES (189, 153379745225248, '水位重复记录稽查M26OVzY1759215413354', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 14:56:53.386', -1, -1, '2025-09-30 14:56:53.386', '2025-09-30 14:56:53.386');
INSERT INTO public.t_ds_task_definition_log VALUES (190, 152439162287424, '转化_水位预警等级校验-2025-09-20', 9, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI0Mzg5NjYxMzQwODAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiYWRzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfU1RBVElPTl9XQVRFUl9MRVZFTF9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjZ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjQxNjE0NTQ2MzYxNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6aKE6K2m562J57qn5qCh6aqMIiwidGFza1ZlcnNpb24iOjl9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:20:02.953', -1, -1, '2025-09-20 00:09:32.174', '2025-09-30 15:20:02.953');
INSERT INTO public.t_ds_task_definition_log VALUES (191, 152439162287424, '转化_水位预警等级校验-2025-09-20', 10, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNDM4OTU3OTg3MTM2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI0Mzg5NjYxMzQwODAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiYWRzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImFkcyJ9LCJkYk5hbWUiOiJhZHMiLCJkYXRhc291cmNlSWQiOjQsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfU1RBVElPTl9XQVRFUl9MRVZFTF9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjZ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjQxNjE0NTQ2MzYxNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6aKE6K2m562J57qn5qCh6aqMIiwidGFza1ZlcnNpb24iOjEwfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:22:37.561', -1, -1, '2025-09-20 00:09:32.174', '2025-09-30 15:22:37.561');
INSERT INTO public.t_ds_task_definition_log VALUES (192, 152642396352832, '转化_水位列转行-2025-09-22', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlRfV0lERSJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiMjAyNV8wMSIsIjIwMjVfMDIiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N5YiX6L2s6KGMIiwidGFza1ZlcnNpb24iOjR9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:26:33.698', -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:26:33.698');
INSERT INTO public.t_ds_task_definition_log VALUES (193, 153381689437728, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":12,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/12","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:30:35.551', -1, -1, '2025-09-30 15:30:35.551', '2025-09-30 15:30:35.551');
INSERT INTO public.t_ds_task_definition_log VALUES (194, 152642396352832, '转化_水位列转行-2025-09-22', 5, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl0sImRiVHlwZSI6IkRNOCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1kd2QiLCJ0YWJsZSI6IkRXRF9TVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVF9XSURFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiXSwid3JpdGVNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjN9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N5YiX6L2s6KGMIiwidGFza1ZlcnNpb24iOjV9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:32:21.216', -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:32:21.216');
INSERT INTO public.t_ds_task_definition_log VALUES (195, 153381999104544, '水位重复记录稽查VsFeLJH1759217760217', 1, '', 147372832245312, 1, 'HTTP', 0, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[]}', 1, 0, 2, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:36:00.245', -1, -1, '2025-09-30 15:36:00.245', '2025-09-30 15:36:00.245');
INSERT INTO public.t_ds_task_definition_log VALUES (196, 152642396352832, '转化_水位列转行-2025-09-22', 6, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl0sImRiVHlwZSI6IkRNOCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1kd2QiLCJ0YWJsZSI6IkRXRF9TVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVF9XSURFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjR9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N5YiX6L2s6KGMIiwidGFza1ZlcnNpb24iOjZ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:36:29.836', -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:36:29.836');
INSERT INTO public.t_ds_task_definition_log VALUES (197, 152642396352832, '转化_水位列转行-2025-09-22', 7, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl0sImRiVHlwZSI6IkRNOCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1kd2QiLCJ0YWJsZSI6IkRXRF9TVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVF9XSURFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjR9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5o6S5bqP6K6w5b2VIiwidGFza1ZlcnNpb24iOjd9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:38:05.925', -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:38:05.925');
INSERT INTO public.t_ds_task_definition_log VALUES (198, 152642396352832, '转化_水位列转行-2025-09-22', 8, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxNDE3MTc5NDU2IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDE0MjM1Mzk1MjAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIl0sImRiVHlwZSI6IkRNOCIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1kd2QiLCJ0YWJsZSI6IkRXRF9TVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVF9XSURFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjR9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0MTEzMzIxNTA0MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N5YiX6L2s6KGMIiwidGFza1ZlcnNpb24iOjh9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6IuaOkuW6j+iusOW9lSIsImNvbXBvbmVudFR5cGUiOiIzNCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQxOTk3MjAyNzUyIiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJjb2x1bW5OYW1lIjoiU1RBVElPTl9JRCIsIm9yZGVyIjoiYXNjIn1dfSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:38:29.538', -1, -1, '2025-09-22 09:25:52.357', '2025-09-30 15:38:29.538');
INSERT INTO public.t_ds_task_definition_log VALUES (199, 152648927077696, '转化_水位行转列-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ3MzA1MjY5NTY4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX05BTUUiLCJBVkdfV0FURVJfTEVWRUwiLCJTVEFUX01PTlRIIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDg3NjMyMTIwOTYiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9OQU1FIiwiQVZHX1dBVEVSX0xFVkVMIiwiU1RBVF9NT05USCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfU1RBVElPTl9XQVRFUl9NT05USF9SRVBPUlQifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fTkFNRSIsIkFWR19XQVRFUl9MRVZFTCIsIlNUQVRfTU9OVEgiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY0Mjc4MDMxMDg0OCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLovazljJZf5rC05L2N6KGM6L2s5YiXIiwidGFza1ZlcnNpb24iOjN9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:39:23.597', -1, -1, '2025-09-22 11:19:31.097', '2025-09-30 15:39:23.597');
INSERT INTO public.t_ds_task_definition_log VALUES (200, 152649930990912, '转化_站点表字符串拼接-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjQ5NzM4NDY4NjcyIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiV0FURVJfVFAiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6IldBVEVSX1RQIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6OCwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NDk3NDEwOTgzMDQiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9DT0RFIiwiU1RBVElPTl9OQU1FIiwiQkFTSU5fQ09ERSIsIlJJVkVSX05BTUUiLCJMT05HSVRVREUiLCJMQVRJVFVERSIsIkFETUlOX1JFR0lPTl9DT0RFIiwiU1RBVFVTIiwiQ1JFQVRFRF9BVCIsIlVQREFURURfQVQiLCJsbmdsYXQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPWR3ZCIsInRhYmxlIjoiRFdEX1NUQVRJT05fTUVSR0UifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSIsIlNUQVRJT05fTkFNRSIsIkJBU0lOX0NPREUiLCJSSVZFUl9OQU1FIiwiTE9OR0lUVURFIiwiTEFUSVRVREUiLCJBRE1JTl9SRUdJT05fQ09ERSIsIlNUQVRVUyIsIkNSRUFURURfQVQiLCJVUERBVEVEX0FUIiwiTE5HTEFUIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoiZHdkIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NDkwNzYwMTc0NzIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueihqOWtl+espuS4suaLvOaOpSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLlrZfmrrXmtL7nlJ/lmagiLCJjb21wb25lbnRUeXBlIjoiMzkiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MjY0OTczOTYyNDc2OCIsInBhcmFtZXRlciI6eyJmaWVsZERlcml2YXRpb25OYW1lIjoibG5nbGF0IiwidGFibGVGaWVsZHMiOlt7ImNvbHVtbk5hbWUiOiJMT05HSVRVREUiLCJvcmRlciI6ImFzYyIsImNhc2VTZW5zaXRpdmUiOmZhbHNlLCJsb2NhbGUiOnRydWUsImNvbGxhdG9yU3RyZW5ndGgiOjAsInByZXNvcnRlZCI6ZmFsc2V9LHsiY29sdW1uTmFtZSI6IkxBVElUVURFIiwib3JkZXIiOiJhc2MiLCJjYXNlU2Vuc2l0aXZlIjpmYWxzZSwibG9jYWxlIjp0cnVlLCJjb2xsYXRvclN0cmVuZ3RoIjowLCJwcmVzb3J0ZWQiOmZhbHNlfV0sImZpZWxkRGVyaXZhdGlvblR5cGUiOiJGSUVMRF9ERVJJVkVfQ09OQ0FUIiwiZGVsaW1pdGVyIjoiLCIsImZpZWxkRGVyaXZhdGlvblN1ZmZpeCI6IiIsImZpZWxkRGVyaXZhdGlvblByZWZpeCI6IiJ9LCJub2RlVmVyc2lvbiI6Mn1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:41:18.321', -1, -1, '2025-09-22 11:37:12.771', '2025-09-30 15:41:18.321');
INSERT INTO public.t_ds_task_definition_log VALUES (201, 152653935271232, '转化_站点数据去重-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTEzNjI4NDgwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiV0FURVJfVFAiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6IldBVEVSX1RQIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6OCwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTAxMTY2Mjg4MDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9DT0RFIiwiU1RBVElPTl9OQU1FIiwiQkFTSU5fQ09ERSIsIlJJVkVSX05BTUUiLCJMT05HSVRVREUiLCJMQVRJVFVERSIsIkFETUlOX1JFR0lPTl9DT0RFIiwiU1RBVFVTIiwiQ1JFQVRFRF9BVCIsIlVQREFURURfQVQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX1NUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSIsIlNUQVRJT05fTkFNRSIsIkJBU0lOX0NPREUiLCJSSVZFUl9OQU1FIiwiTE9OR0lUVURFIiwiTEFUSVRVREUiLCJBRE1JTl9SRUdJT05fQ09ERSIsIlNUQVRVUyIsIkNSRUFURURfQVQiLCJVUERBVEVEX0FUIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoib2RzIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTAxMTE4NjgyMjQiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueaVsOaNruWOu+mHjSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLovazmjaLnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMzEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MzM4MjM5MjU0ODg5NiIsInBhcmFtZXRlciI6eyJ0YWJsZUZpZWxkcyI6W3sibmFtZSI6IuagueaNrmlk5ZKM57yW56CB5Y676YeNIiwicnVsZU5hbWUiOiLmjInnu4TlkIjlrZfmrrXljrvph43vvIjkv53nlZnmnIDmlrDmiJbpppbmnaHvvIkiLCJydWxlQ29kZSI6IjAyOSIsInN0YXR1cyI6IjEiLCJ3aGVyZUNsYXVzZSI6IiIsImNvbHVtbnMiOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzOSIsInJ1bGVDb25maWciOiJ7XCJjb2x1bW5zXCI6W1wiU1RBVElPTl9JRFwiLFwiU1RBVElPTl9DT0RFXCJdLFwic3RyaW5nVmFsdWVcIjpbe1wic29ydFwiOjEsXCJjb2x1bW5zXCI6XCJTVEFUSU9OX0lEXCIsXCJ0eXBlXCI6XCIwXCJ9XSxcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIumHjeWkjeiusOW9leWIoOmZpOexu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJLRUVQX0xBVEVTVF9PUl9GSVJTVCIsInBhcmVudE5hbWUiOiLph43lpI3orrDlvZXliKDpmaTnsbsifV0sIndoZXJlIjoiIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:43:52.594', -1, -1, '2025-09-22 12:46:47.632', '2025-09-30 15:43:52.594');
INSERT INTO public.t_ds_task_definition_log VALUES (202, 152653935271232, '转化_站点数据去重-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjUwMTEzNjI4NDgwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIlNUQVRJT05fSUQiLCJTVEFUSU9OX0NPREUiLCJTVEFUSU9OX05BTUUiLCJCQVNJTl9DT0RFIiwiUklWRVJfTkFNRSIsIkxPTkdJVFVERSIsIkxBVElUVURFIiwiQURNSU5fUkVHSU9OX0NPREUiLCJTVEFUVVMiLCJDUkVBVEVEX0FUIiwiVVBEQVRFRF9BVCJdLCJkYlR5cGUiOiJETTgiLCJyZWFkZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiV0FURVJfVFAiLCJwYXNzd29yZCI6ImxtT3h4KzN3RHUzb1VNeFJLQVIzTFE9PSIsImRibmFtZSI6IldBVEVSX1RQIn0sImRiTmFtZSI6IldBVEVSX1RQIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwiZGF0ZUluY3JlbWVudENvbmZpZyI6eyJsb2dpYyI6ImFuZCIsImRhdGVGb3JtYXQiOiJ5eXl5LU1NLWRkIiwiY29sdW1uIjpbXX0sImlkSW5jcmVtZW50Q29uZmlnIjp7ImluY3JlbWVudFN0YXJ0IjoiIn0sImRhdGFzb3VyY2VJZCI6OCwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IlNUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTAxMTY2Mjg4MDAiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoib2RzIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJvZHMifSwiZGJOYW1lIjoib2RzIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6Im9kcyJ9LCJkYk5hbWUiOiJvZHMiLCJkYXRhc291cmNlSWQiOjEsImNvbHVtbiI6WyJTVEFUSU9OX0lEIiwiU1RBVElPTl9DT0RFIiwiU1RBVElPTl9OQU1FIiwiQkFTSU5fQ09ERSIsIlJJVkVSX05BTUUiLCJMT05HSVRVREUiLCJMQVRJVFVERSIsIkFETUlOX1JFR0lPTl9DT0RFIiwiU1RBVFVTIiwiQ1JFQVRFRF9BVCIsIlVQREFURURfQVQiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPW9kcyIsInRhYmxlIjoiT0RTX1NUQVRJT04ifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSIsIlNUQVRJT05fTkFNRSIsIkJBU0lOX0NPREUiLCJSSVZFUl9OQU1FIiwiTE9OR0lUVURFIiwiTEFUSVRVREUiLCJBRE1JTl9SRUdJT05fQ09ERSIsIlNUQVRVUyIsIkNSRUFURURfQVQiLCJVUERBVEVEX0FUIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoib2RzIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTAxMTE4NjgyMjQiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi6L2s5YyWX+ermeeCueaVsOaNruWOu+mHjSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W3sibm9kZU5hbWUiOiLovazmjaLnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMzEiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJub2RlQ29kZSI6IjE1MzM4MjM5MjU0ODg5NiIsInBhcmFtZXRlciI6eyJ0YWJsZUZpZWxkcyI6W3sibmFtZSI6IuagueaNrmlk5ZKM57yW56CB5Y676YeNIiwicnVsZU5hbWUiOiLmjInnu4TlkIjlrZfmrrXljrvph43vvIjkv53nlZnmnIDmlrDmiJbpppbmnaHvvIkiLCJydWxlQ29kZSI6IjAyOSIsInN0YXR1cyI6IjEiLCJ3aGVyZUNsYXVzZSI6IiIsImNvbHVtbnMiOlsiU1RBVElPTl9JRCIsIlNUQVRJT05fQ09ERSJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzOSIsInJ1bGVDb25maWciOiJ7XCJjb2x1bW5zXCI6W1wiU1RBVElPTl9JRFwiLFwiU1RBVElPTl9DT0RFXCJdLFwic3RyaW5nVmFsdWVcIjpbe1wic29ydFwiOjEsXCJjb2x1bW5zXCI6XCJTVEFUSU9OX0lEXCIsXCJ0eXBlXCI6XCIwXCJ9XSxcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIumHjeWkjeiusOW9leWIoOmZpOexu1wifSIsImlkIjoiIiwicnVsZVR5cGUiOiJLRUVQX0xBVEVTVF9PUl9GSVJTVCIsInBhcmVudE5hbWUiOiLph43lpI3orrDlvZXliKDpmaTnsbsifV0sIndoZXJlIjoiIn0sIm5vZGVWZXJzaW9uIjoxfV19","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:44:07.456', -1, -1, '2025-09-22 12:46:47.632', '2025-09-30 15:44:07.456');
INSERT INTO public.t_ds_task_definition_log VALUES (203, 152654997784896, '多表_更新写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc2MzIxMDg4IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU0NDc4MTM2NjQwIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfVVBEQVRFIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIkJBU0lOX0NPREUiLCJTVEFUX01PTlRIIiwiU1RBVElPTl9DTlQiLCJBVkdfTEVWRUwiLCJNQVhfTEVWRUwiLCJNSU5fTEVWRUwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NDQ3MzM4MDE2MCIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf5pu05paw5YaZIiwidGFza1ZlcnNpb24iOjN9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:45:32.483', -1, -1, '2025-09-22 13:05:39.183', '2025-09-30 15:45:32.483');
INSERT INTO public.t_ds_task_definition_log VALUES (204, 152655297790272, '多表_全量写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjYzNjUwMTEyIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjY2Mzg0MTkyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfRlVMTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJCQVNJTl9DT0RFIiwiU1RBVF9NT05USCIsIlNUQVRJT05fQ05UIiwiQVZHX0xFVkVMIiwiTUFYX0xFVkVMIiwiTUlOX0xFVkVMIl0sIndyaXRlTW9kZVR5cGUiOjIsInVzZXJuYW1lIjoiYWRzIn0sIm5vZGVWZXJzaW9uIjoyfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTUyMzg2NzQ3NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5aSa6KGoX+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjoyfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:47:08.542', -1, -1, '2025-09-22 13:10:50.235', '2025-09-30 15:47:08.542');
INSERT INTO public.t_ds_task_definition_log VALUES (205, 152655297790272, '多表_全量写-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjYzNjUwMTEyIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1MjY2Mzg0MTkyIiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfRlVMTCJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJCQVNJTl9DT0RFIiwiU1RBVF9NT05USCIsIlNUQVRJT05fQ05UIiwiQVZHX0xFVkVMIiwiTUFYX0xFVkVMIiwiTUlOX0xFVkVMIl0sIndyaXRlTW9kZVR5cGUiOjEsInVzZXJuYW1lIjoiYWRzIn0sIm5vZGVWZXJzaW9uIjozfSwiY29uZmlnIjp7InJlc291cmNlVXJsIjoiL2RvbHBoaW5zY2hlZHVsZXIvIiwidGFza0luZm8iOnsidGFza0NvZGUiOiIxNTI2NTUyMzg2NzQ3NTIiLCJwcm9qZWN0Q29kZSI6MTUyMzE3NzkwOTc1NzEyLCJuYW1lIjoi5aSa6KGoX+WFqOmHj+WGmSIsInRhc2tWZXJzaW9uIjozfSwicmFiYml0bXEiOnsiaG9zdCI6InJhYmJpdG1xIiwicGFzc3dvcmQiOiJFal5pVU5GTHA5TVFvdWMxIiwicG9ydCI6NTY3MiwidXNlcm5hbWUiOiJhZG1pbiJ9fSwidHJhbnNpdGlvbiI6W119","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:47:19.506', -1, -1, '2025-09-22 13:10:50.235', '2025-09-30 15:47:19.506');
INSERT INTO public.t_ds_task_definition_log VALUES (206, 152655501865280, '多表_追加写-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NDI2MjMyNjQwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbImJhc2luX2NvZGUiLCJzdGF0X21vbnRoIiwic3RhdGlvbl9jbnQiLCJhdmdfbGV2ZWwiLCJtYXhfbGV2ZWwiLCJtaW5fbGV2ZWwiXSwiZGJUeXBlIjoiRE04IiwicmVhZGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6IldBVEVSX1RQIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJXQVRFUl9UUCJ9LCJkYk5hbWUiOiJXQVRFUl9UUCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsImRhdGVJbmNyZW1lbnRDb25maWciOnsibG9naWMiOiJhbmQiLCJkYXRlRm9ybWF0IjoieXl5eS1NTS1kZCIsImNvbHVtbiI6W119LCJpZEluY3JlbWVudENvbmZpZyI6eyJpbmNyZW1lbnRTdGFydCI6IiJ9LCJkYXRhc291cmNlSWQiOjgsIndoZXJlIjoiIiwiY29ubmVjdGlvbiI6eyJxdWVyeVNxbCI6IlNFTEVDVFxuICBzLlwiQkFTSU5fQ09ERVwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBiYXNpbl9jb2RlLFxuICByLlwiU1RBVF9NT05USFwiICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBBUyBzdGF0X21vbnRoLFxuICBDT1VOVChESVNUSU5DVCBzLlwiU1RBVElPTl9JRFwiKSAgICAgICAgICAgICAgICAgICBBUyBzdGF0aW9uX2NudCwgICAgIFxuICBST1VORChBVkcoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBhdmdfbGV2ZWwsIFxuICBST1VORChNQVgoVE9fTlVNQkVSKE5VTExJRihyLlwiQVZHX1dBVEVSX0xFVkVMXCIsICcnKSkpLCAzKSBBUyBtYXhfbGV2ZWwsXG4gIFJPVU5EKE1JTihUT19OVU1CRVIoTlVMTElGKHIuXCJBVkdfV0FURVJfTEVWRUxcIiwgJycpKSksIDMpIEFTIG1pbl9sZXZlbFxuRlJPTSBcIldBVEVSX1RQXCIuXCJTVEFUSU9OXCIgc1xuSk9JTiBcIldBVEVSX1RQXCIuXCJTVEFUSU9OX1dBVEVSX01PTlRIX1JFUE9SVFwiIHJcbiAgT04gci5cIlNUQVRJT05fSURcIiA9IHMuXCJTVEFUSU9OX0lEXCJcbi0tIOWmgumcgOi/h+a7pOaciOS7ve+8jOWPr+WKoO+8mldIRVJFIHIuXCJTVEFUX01PTlRIXCIgQkVUV0VFTiAnMjAyNS0wMScgQU5EICcyMDI1LTEyJ1xuR1JPVVAgQllcbiAgcy5cIkJBU0lOX0NPREVcIixcbiAgci5cIlNUQVRfTU9OVEhcIlxuT1JERVIgQllcbiAgci5cIlNUQVRfTU9OVEhcIixcbiAgcy5cIkJBU0lOX0NPREVcIiIsImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6MSwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6Mn0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU1NDI4MjIwMjI0IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImFkcyIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiYWRzIn0sImRiTmFtZSI6ImFkcyIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJhZHMifSwiZGJOYW1lIjoiYWRzIiwiZGF0YXNvdXJjZUlkIjo0LCJjb2x1bW4iOlsiYmFzaW5fY29kZSIsInN0YXRfbW9udGgiLCJzdGF0aW9uX2NudCIsImF2Z19sZXZlbCIsIm1heF9sZXZlbCIsIm1pbl9sZXZlbCJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9YWRzIiwidGFibGUiOiJBRFNfQkFTSU5fV0FURVJfTU9OVEhfUkVQT1JUX1dJREVfQVBQRU5EIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJ0YXJnZXRfY29sdW1uIjpbIkJBU0lOX0NPREUiLCJTVEFUX01PTlRIIiwiU1RBVElPTl9DTlQiLCJBVkdfTEVWRUwiLCJNQVhfTEVWRUwiLCJNSU5fTEVWRUwiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJhZHMifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NTQyNDg1MzMxMiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLlpJrooahf6L+95Yqg5YaZIiwidGFza1ZlcnNpb24iOjJ9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbXX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 15:48:35.655', -1, -1, '2025-09-22 13:14:24.026', '2025-09-30 15:48:35.655');
INSERT INTO public.t_ds_task_definition_log VALUES (214, 152658250408256, '清洗_水位缺失补全-2025-09-22', 3, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkwODA5NjY0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjoxMiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IldBVEVSX0xFVkVMIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkzNjU3NDA4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImR3ZCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGF0YXNvdXJjZUlkIjoyLCJjb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPWR3ZCIsInRhYmxlIjoiRFdEX1dBVEVSX0xFVkVMX0NMRUFOX01JU1NJTkcifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NzY4OTUzMjczNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLmuIXmtJdf5rC05L2N5re75Yqg5Y2V5L2NIiwidGFza1ZlcnNpb24iOjN9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6Iui9rOaNoue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIzMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4MDU4MTY5NjY0IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJuYW1lIjoi5rC05L2N5re75Yqg5Y2V5L2NIiwicnVsZU5hbWUiOiLlrZfmrrXliY3nvIAv5ZCO57yA57uf5LiAIiwicnVsZUNvZGUiOiIwMTAiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwic3RyaW5nVmFsdWVcIjpcIuexs1wiLFwiaGFuZGxlVHlwZVwiOlwiMlwiLFwicGFyZW50TmFtZVwiOlwi5qC85byP5qCH5YeG5YyW57G7XCJ9IiwiaWQiOiIiLCJwYXJlbnROYW1lIjoi5qC85byP5qCH5YeG5YyW57G7IiwicnVsZVR5cGUiOiJBRERfUFJFRklYX1NVRkZJWCJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 16:03:07.1', -1, -1, '2025-09-22 14:02:24.009', '2025-09-30 16:03:07.1');
INSERT INTO public.t_ds_task_definition_log VALUES (215, 152658250408256, '清洗_水位缺失补全-2025-09-22', 4, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkwODA5NjY0IiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjoxMiwid2hlcmUiOiIiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9V0FURVJfVFAiLCJ0YWJsZSI6IldBVEVSX0xFVkVMIn0sImJhdGNoU2l6ZSI6IjEwMjQiLCJyZWFkTW9kZVR5cGUiOiIxIiwidXNlcm5hbWUiOiJXQVRFUl9UUCJ9LCJub2RlVmVyc2lvbiI6MX0sIndyaXRlciI6eyJub2RlTmFtZSI6IuihqOi+k+WHuue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiI5MSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU3NjkzNjU3NDA4IiwicGFyYW1ldGVyIjp7InBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4Iiwic2VsZWN0ZWRDb2x1bW5zIjpbXSwid3JpdGVyUHJvcGVydHkiOnsiZGF0YXNvdXJjZUNvbmZpZyI6eyJ1c2VybmFtZSI6ImR3ZCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiZHdkIn0sImRiTmFtZSI6ImR3ZCIsImRiVHlwZSI6IkRNOCIsImhvc3QiOiJkbTgtZGVtbyIsInBhc3N3b3JkIjoiczJMS3I2TE1ReFZEVFF4IiwicG9ydCI6NTIzNiwidXNlcm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGF0YXNvdXJjZUlkIjoyLCJjb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwiZGJUeXBlIjoiRE04IiwiY29ubmVjdGlvbiI6eyJqZGJjVXJsIjoiamRiYzpkbTovL2RtOC1kZW1vOjUyMzY/c2NoZW1hPWR3ZCIsInRhYmxlIjoiRFdEX1dBVEVSX0xFVkVMX0NMRUFOX01JU1NJTkcifSwiYmF0Y2hTaXplIjoiMTAyNCIsInRhcmdldF9jb2x1bW4iOlsiSUQiLCJTVEFUSU9OX0NPREUiLCJPQlNfVElNRSIsIldBVEVSX0xFVkVMIiwiUVVBTElUWV9GTEFHIiwiVFMiXSwid3JpdGVNb2RlVHlwZSI6MiwidXNlcm5hbWUiOiJkd2QifSwibm9kZVZlcnNpb24iOjJ9LCJjb25maWciOnsicmVzb3VyY2VVcmwiOiIvZG9scGhpbnNjaGVkdWxlci8iLCJ0YXNrSW5mbyI6eyJ0YXNrQ29kZSI6IjE1MjY1NzY4OTUzMjczNiIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5hbWUiOiLmuIXmtJdf5rC05L2N57y65aSx6KGl5YWoIiwidGFza1ZlcnNpb24iOjR9LCJyYWJiaXRtcSI6eyJob3N0IjoicmFiYml0bXEiLCJwYXNzd29yZCI6IkVqXmlVTkZMcDlNUW91YzEiLCJwb3J0Ijo1NjcyLCJ1c2VybmFtZSI6ImFkbWluIn19LCJ0cmFuc2l0aW9uIjpbeyJub2RlTmFtZSI6Iui9rOaNoue7hOS7tiIsImNvbXBvbmVudFR5cGUiOiIzMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4MDU4MTY5NjY0IiwicGFyYW1ldGVyIjp7InRhYmxlRmllbGRzIjpbeyJuYW1lIjoi5rC05L2N5re75Yqg5Y2V5L2NIiwicnVsZU5hbWUiOiLlrZfmrrXliY3nvIAv5ZCO57yA57uf5LiAIiwicnVsZUNvZGUiOiIwMTAiLCJzdGF0dXMiOiIxIiwid2hlcmVDbGF1c2UiOiIiLCJjb2x1bW5zIjpbIldBVEVSX0xFVkVMIl0sInRhYmxlTmFtZSI6IiIsInJ1bGVEZXNjIjoiIiwidHlwZSI6IjUiLCJydWxlQ29uZmlnIjoie1wiY29sdW1uc1wiOltcIldBVEVSX0xFVkVMXCJdLFwic3RyaW5nVmFsdWVcIjpcIuexs1wiLFwiaGFuZGxlVHlwZVwiOlwiMlwiLFwicGFyZW50TmFtZVwiOlwi5qC85byP5qCH5YeG5YyW57G7XCJ9IiwiaWQiOiIiLCJwYXJlbnROYW1lIjoi5qC85byP5qCH5YeG5YyW57G7IiwicnVsZVR5cGUiOiJBRERfUFJFRklYX1NVRkZJWCJ9XSwid2hlcmUiOiIifSwibm9kZVZlcnNpb24iOjJ9XX0=","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 16:03:29.962', -1, -1, '2025-09-22 14:02:24.009', '2025-09-30 16:03:29.962');
INSERT INTO public.t_ds_task_definition_log VALUES (216, 152658885655872, '清洗_水位异常值处理-2025-09-22', 2, '', 152317790975712, 1, 'SPARK', 0, '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4NDkxMjIzMzYwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTMyNTI2NzIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfV0FURVJfTEVWRUxfQ0xFQU5fT1VUTElFUiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6ImR3ZCJ9LCJub2RlVmVyc2lvbiI6Mn0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU4NDkwMDU0OTc2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3lvILluLjlgLzlpITnkIYiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifX0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTE4NjMxMDQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLlvILluLjlgLzliZTpmaQiLCJydWxlTmFtZSI6IuaVsOWAvOi+ueeVjOiwg+aVtCIsInJ1bGVDb2RlIjoiMDAxIiwic3RhdHVzIjoiMSIsIndoZXJlQ2xhdXNlIjoiIiwiY29sdW1ucyI6WyJXQVRFUl9MRVZFTCJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzIiwicnVsZUNvbmZpZyI6IntcImNvbHVtbnNcIjpbXCJXQVRFUl9MRVZFTFwiXSxcIm1pblwiOlwiMFwiLFwibWF4XCI6XCIxMDBcIixcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIuW8guW4uOWAvOS/ruato+exu1wifSIsImlkIjoiIiwicGFyZW50TmFtZSI6IuW8guW4uOWAvOS/ruato+exuyIsInJ1bGVUeXBlIjoiV0lUSElOX0JPVU5EQVJZIn1dLCJ3aGVyZSI6IiJ9LCJub2RlVmVyc2lvbiI6Mn1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT"}', 1, 0, NULL, 'default', 133155949418208, 0, 0, NULL, NULL, 0, 0, NULL, 1, 0, 0, '2025-09-30 16:05:39.349', -1, -1, '2025-09-22 14:13:36.295', '2025-09-30 16:05:39.349');
INSERT INTO public.t_ds_task_definition_log VALUES (217, 153383838261792, 'Kingbase_水位日统计', 1, '', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"KINGBASE","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qdata_dev","database":"test","other":{},"port":54321,"connectType":"ORACLE_SERVICE_NAME","host":"kingbase","type":"KINGBASE","userName":"qdata_dev"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 1, NULL, 1, 0, 0, '2025-09-30 16:08:34.03', -1, -1, '2025-09-30 16:08:34.03', '2025-09-30 16:08:34.03');
INSERT INTO public.t_ds_task_definition_log VALUES (218, 153383838261792, 'Kingbase_水位日统计', 2, '【Kingbase 存储过程】查询水位实时数据，生成每日统计报表  ', 152317790975712, 1, 'SQL', 0, '{"localParams":[],"resourceList":[],"type":"KINGBASE","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"qdata_dev","database":"test","other":{},"port":54321,"connectType":"ORACLE_SERVICE_NAME","host":"kingbase","type":"KINGBASE","userName":"qdata_dev"}}', 1, 0, 2, 'default', 133155949418208, 0, 1, NULL, NULL, 0, 1, NULL, 1, 0, 0, '2025-09-30 16:09:14.425', -1, -1, '2025-09-30 16:08:34.03', '2025-09-30 16:09:14.425');


--
-- Data for Name: t_ds_task_group; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_task_group_queue; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_task_instance; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_task_instance VALUES (145, '站点编码非空与格式稽查sjuNHe11759215156767', 'HTTP', 0, 153379496920608, 1, 142, '站点编码非空与格式稽查sjuNHe11759215156767-1-20250930145239258', 147372832245312, 7, '2025-09-30 14:52:39.305', '2025-09-30 14:52:39.361', '2025-09-30 14:52:41.041', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/153379497094688_1/142/145', '/opt/dolphinscheduler/logs/20250930/153379497094688/1/142/145.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":9,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/9","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 14:52:39.305', 0, 0, '[{"prop":"站点编码非空与格式稽查sjuNHe11759215156767.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (5, '测试9LMXA6X1756874378318', 'HTTP', 0, 150982633305856, 1, 5, '测试9LMXA6X1756874378318-2-20250903123942541', 147372832245312, 7, '2025-09-03 12:39:42.578', '2025-09-03 12:39:42.679', '2025-09-03 12:39:43.644', '172.26.0.16:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/150982603138816_2/5/5', '/opt/dolphinscheduler/logs/20250903/150982603138816/2/5/5.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-03 12:39:42.578', 0, 0, '[{"prop":"测试9LMXA6X1756874378318.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (70, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618', 'HTTP', 0, 152662937492800, 1, 70, '水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618-3-20250922152413821', 147372832245312, 7, '2025-09-22 15:24:13.844', '2025-09-22 15:24:13.871', '2025-09-22 15:24:14.136', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152416165530944_3/70/70', '/opt/dolphinscheduler/logs/20250922/152416165530944/3/70/70.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-22 15:24:13.844', 0, 0, '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418BlbAoh41758525850618.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (147, '水位重复记录稽查M26OVzY1759215413354', 'HTTP', 0, 153379745225248, 1, 144, '水位重复记录稽查M26OVzY1759215413354-1-20250930145657939', 147372832245312, 7, '2025-09-30 14:56:57.957', '2025-09-30 14:56:57.993', '2025-09-30 14:56:58.104', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/153379745251872_1/144/147', '/opt/dolphinscheduler/logs/20250930/153379745251872/1/144/147.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 14:56:57.957', 0, 0, '[{"prop":"水位重复记录稽查M26OVzY1759215413354.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (19, '2323_user_20250916172917OMdTTDk1758014992747', 'HTTP', 0, 152150622975936, 1, 19, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916172957244', 147372832245312, 7, '2025-09-16 17:29:57.262', '2025-09-16 17:29:57.283', '2025-09-16 17:29:57.336', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152150622981056_1/19/19', '/opt/dolphinscheduler/logs/20250916/152150622981056/1/19/19.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:29:57.262', 0, 0, '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (18, '2323_user_20250916172917OMdTTDk1758014992747', 'HTTP', 0, 152150622975936, 1, 18, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916172954229', 147372832245312, 7, '2025-09-16 17:29:54.243', '2025-09-16 17:29:54.271', '2025-09-16 17:29:56.099', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152150622981056_1/18/18', '/opt/dolphinscheduler/logs/20250916/152150622981056/1/18/18.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:29:54.243', 0, 0, '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (23, '水文监测数据发现Rg0XxWU1758271770687', 'HTTP', 0, 152413555441984, 1, 23, '水文监测数据发现Rg0XxWU1758271770687-1-20250919164939811', 134799536571008, 7, '2025-09-19 16:49:39.824', '2025-09-19 16:49:39.849', '2025-09-19 16:49:40.067', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152413555449152_1/23/23', '/opt/dolphinscheduler/logs/20250919/152413555449152/1/23/23.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":2,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/2","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 16:49:39.824', 0, 0, '[{"prop":"水文监测数据发现Rg0XxWU1758271770687.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (14, '232332', 'FLINK', 0, 152150248157120, 1, 14, '232332-1-20250916172439585', 141883958809440, 7, '2025-09-16 17:24:39.658', '2025-09-16 17:24:39.779', '2025-09-16 17:24:52.909', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/141883958809440/152150248157120_1/14/14', '/opt/dolphinscheduler/logs/20250916/152150248157120/1/14/14.log', 0, 0, 1272, '', '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;","conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 1, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:24:39.658', 0, 0, '[]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (17, '232332Cjy7WIv1758014906806', 'HTTP', 0, 152150534972352, 1, 17, '232332Cjy7WIv1758014906806-2-20250916172831065', 134799536571008, 7, '2025-09-16 17:28:31.088', '2025-09-16 17:28:31.137', '2025-09-16 17:28:32.538', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152150518585280_2/17/17', '/opt/dolphinscheduler/logs/20250916/152150518585280/2/17/17.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:28:31.088', 0, 0, '[{"prop":"232332Cjy7WIv1758014906806.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (15, '232332', 'FLINK', 0, 152150248157120, 1, 15, '232332-1-20250916172528723', 141883958809440, 7, '2025-09-16 17:25:28.744', '2025-09-16 17:25:28.769', '2025-09-16 17:25:39.858', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/141883958809440/152150248157120_1/15/15', '/opt/dolphinscheduler/logs/20250916/152150248157120/1/15/15.log', 0, 0, 1676, '', '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;","conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 1, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:25:28.744', 0, 0, '[]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (16, '232332', 'FLINK', 0, 152150248157120, 1, 16, '232332-1-20250916172703895', 141883958809440, 7, '2025-09-16 17:27:03.917', '2025-09-16 17:27:03.944', '2025-09-16 17:27:16.025', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/141883958809440/152150248157120_1/16/16', '/opt/dolphinscheduler/logs/20250916/152150248157120/1/16/16.log', 0, 0, 2090, '', '{"localParams":[],"rawScript":"-- 源表：JDBC 读取\r\nCREATE TABLE source_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver''\r\n);\r\n\r\n-- 目标表：JDBC 写入\r\nCREATE TABLE sink_orders (\r\n  order_id INT NOT NULL,\r\n  customer_name STRING,\r\n  order_amount DECIMAL(10,2),\r\n  order_date DATE,\r\n  PRIMARY KEY (order_id) NOT ENFORCED\r\n) WITH (\r\n  ''connector'' = ''jdbc'',\r\n  ''url'' = ''jdbc:mysql://110.42.38.62:3306/target_db?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai'',\r\n  ''table-name'' = ''target_orders_copy1'',\r\n  ''username'' = ''root'',\r\n  ''password'' = ''wangming1114'',\r\n  ''driver'' = ''com.mysql.cj.jdbc.Driver'',\r\n  ''sink.buffer-flush.max-rows'' = ''1000'',\r\n  ''sink.buffer-flush.interval'' = ''2s''\r\n);\r\n\r\n-- 一次性同步数据\r\nINSERT INTO sink_orders\r\nSELECT\r\n  order_id,\r\n  customer_name,\r\n  order_amount,\r\n  order_date\r\nFROM source_orders;","resourceList":[],"programType":"SQL","mainClass":"","deployMode":"local","yarnQueue":"","flinkVersion":">=1.13","jobManagerMemory":"1G","taskManagerMemory":"2G","slot":1,"taskManager":2,"parallelism":1,"initScript":"SET execution.runtime-mode = batch;","conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 1, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:27:03.917', 0, 0, '[]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (20, '2323_user_20250916172917OMdTTDk1758014992747', 'HTTP', 0, 152150622975936, 1, 20, '2323_user_20250916172917OMdTTDk1758014992747-1-20250916173004269', 147372832245312, 7, '2025-09-16 17:30:04.296', '2025-09-16 17:30:04.317', '2025-09-16 17:30:04.379', '172.24.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152150622981056_1/20/20', '/opt/dolphinscheduler/logs/20250916/152150622981056/1/20/20.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-16 17:30:04.296', 0, 0, '[{"prop":"2323_user_20250916172917OMdTTDk1758014992747.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (21, '工程监管数据发现uVOWKbY1758271591783', 'HTTP', 0, 152413380820288, 1, 21, '工程监管数据发现uVOWKbY1758271591783-1-20250919164634748', 134799536571008, 7, '2025-09-19 16:46:35.169', '2025-09-19 16:46:36.15', '2025-09-19 16:46:37.053', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152413381367104_1/21/21', '/opt/dolphinscheduler/logs/20250919/152413381367104/1/21/21.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 16:46:35.169', 0, 0, '[{"prop":"工程监管数据发现uVOWKbY1758271591783.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (22, '工程监管数据发现wSAxLkg1758271769213', 'HTTP', 0, 152413553931584, 1, 22, '工程监管数据发现wSAxLkg1758271769213-2-20250919164937792', 134799536571008, 7, '2025-09-19 16:49:37.811', '2025-09-19 16:49:37.841', '2025-09-19 16:49:38.167', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152413381367104_2/22/22', '/opt/dolphinscheduler/logs/20250919/152413381367104/2/22/22.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":1,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/1","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 16:49:37.811', 0, 0, '[{"prop":"工程监管数据发现wSAxLkg1758271769213.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (24, '水质监测数据发现OemKK9R1758271772314', 'HTTP', 0, 152413557107008, 1, 24, '水质监测数据发现OemKK9R1758271772314-1-20250919164942828', 134799536571008, 7, '2025-09-19 16:49:42.845', '2025-09-19 16:49:42.871', '2025-09-19 16:49:44.709', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152413557117248_1/24/24', '/opt/dolphinscheduler/logs/20250919/152413557117248/1/24/24.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/3","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 16:49:42.845', 0, 0, '[{"prop":"水质监测数据发现OemKK9R1758271772314.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (25, '水资源数据发现xAxpna41758271796919', 'HTTP', 0, 152413579804992, 1, 25, '水资源数据发现xAxpna41758271796919-1-20250919164958873', 134799536571008, 7, '2025-09-19 16:49:58.887', '2025-09-19 16:49:58.916', '2025-09-19 16:49:59.57', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/134799536571008/152413579812160_1/25/25', '/opt/dolphinscheduler/logs/20250919/152413579812160/1/25/25.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":4,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-api:8080/da/daDiscoveryTask/runDaDiscoveryTask/4","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 16:49:58.887', 0, 0, '[{"prop":"水资源数据发现xAxpna41758271796919.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (28, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'HTTP', 0, 152416173958464, 1, 28, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810-2-20250919173519630', 147372832245312, 7, '2025-09-19 17:35:19.683', '2025-09-19 17:35:19.74', '2025-09-19 17:35:19.849', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152416165530944_2/28/28', '/opt/dolphinscheduler/logs/20250919/152416165530944/2/28/28.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 17:35:19.683', 0, 0, '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (29, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810', 'HTTP', 0, 152416173958464, 1, 29, '水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810-2-20250919173520670', 147372832245312, 7, '2025-09-19 17:35:20.683', '2025-09-19 17:35:20.705', '2025-09-19 17:35:20.757', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152416165530944_2/29/29', '/opt/dolphinscheduler/logs/20250919/152416165530944/2/29/29.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":5,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/5","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-19 17:35:20.683', 0, 0, '[{"prop":"水资源测站点数据（原始库）_ods_wr_station_20250919173418wjZYu8W1758274517810.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (103, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 102, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250923000001506', 147372832245312, 7, '2025-09-23 00:00:01.634', '2025-09-23 00:00:01.953', '2025-09-23 00:00:02.82', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/102/103', '/opt/dolphinscheduler/logs/20250923/152665378283840/1/102/103.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-23 00:00:01.634', 0, 0, '[{"prop":"水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (150, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'HTTP', 0, 153381689437728, 1, 147, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526-1-20250930153037751', 147372832245312, 7, '2025-09-30 15:30:37.783', '2025-09-30 15:30:37.837', '2025-09-30 15:30:38.056', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/153381689458208_1/147/150', '/opt/dolphinscheduler/logs/20250930/153381689458208/1/147/150.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":12,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/12","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 15:30:37.783', 0, 0, '[{"prop":"水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (82, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 82, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250922160728245', 147372832245312, 7, '2025-09-22 16:07:28.263', '2025-09-22 16:07:28.295', '2025-09-22 16:07:28.476', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/82/82', '/opt/dolphinscheduler/logs/20250922/152665378283840/1/82/82.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-22 16:07:28.263', 0, 0, '[{"prop":"水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (151, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526', 'HTTP', 0, 153381689437728, 1, 148, '水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526-1-20250930153040784', 147372832245312, 7, '2025-09-30 15:30:40.826', '2025-09-30 15:30:40.856', '2025-09-30 15:30:40.928', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/153381689458208_1/148/151', '/opt/dolphinscheduler/logs/20250930/153381689458208/1/148/151.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":12,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/12","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 15:30:40.826', 0, 0, '[{"prop":"水文监测水位数据（原始库）_ODS_HYD_WATER_LEVEL_20250930152908myUTzDQ1759217435526.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (55, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745', 'HTTP', 0, 152657963461952, 1, 55, '水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745-1-20250922135719280', 147372832245312, 7, '2025-09-22 13:57:19.3', '2025-09-22 13:57:19.325', '2025-09-22 13:57:19.385', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152657963472192_1/55/55', '/opt/dolphinscheduler/logs/20250922/152657963472192/1/55/55.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":3,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/3","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-22 13:57:19.3', 0, 0, '[{"prop":"水资源水位数据（原始库）_ods_wr_water_level_20250919170433pRvgE0N1758520637745.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (154, '水位重复记录稽查VsFeLJH1759217760217', 'HTTP', 0, 153381999104544, 1, 151, '水位重复记录稽查VsFeLJH1759217760217-2-20250930153605909', 147372832245312, 7, '2025-09-30 15:36:05.932', '2025-09-30 15:36:05.983', '2025-09-30 15:36:06.101', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/153379745251872_2/151/154', '/opt/dolphinscheduler/logs/20250930/153379745251872/2/151/154.log', 0, 0, 0, NULL, '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":11,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/11","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 15:36:05.932', 0, 0, '[{"prop":"水位重复记录稽查VsFeLJH1759217760217.response","direct":"OUT","type":"VARCHAR","value":"{\"msg\":\"操作成功\",\"code\":200}"}]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (133, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 132, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 147372832245312, 6, '2025-09-26 13:55:56.472', '2025-09-26 13:55:56.9', '2025-09-26 13:56:06.974', '172.18.0.13:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/132/133', '/opt/dolphinscheduler/logs/20250926/152665378283840/1/132/133.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-26 13:55:56.472', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (134, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 130, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 147372832245312, 6, '2025-09-26 13:55:56.472', '2025-09-26 13:55:56.904', '2025-09-26 13:56:06.972', '172.18.0.13:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/130/134', '/opt/dolphinscheduler/logs/20250926/152665378283840/1/130/134.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-26 13:55:56.472', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (135, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 131, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250926135556239', 147372832245312, 6, '2025-09-26 13:55:56.472', '2025-09-26 13:55:56.93', '2025-09-26 13:56:06.971', '172.18.0.13:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/131/135', '/opt/dolphinscheduler/logs/20250926/152665378283840/1/131/135.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-26 13:55:56.472', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (168, 'MySQL_水位日统计', 'SQL', 0, 152672766952768, 1, 165, 'MySQL_水位日统计-1-20250930160854082', 152317790975712, 6, '2025-09-30 16:08:54.111', '2025-09-30 16:08:54.458', '2025-09-30 16:08:57.334', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/152317790975712/152672766952768_1/165/168', '/opt/dolphinscheduler/logs/20250930/152672766952768/1/165/168.log', 0, 0, 0, '', '{"localParams":[],"resourceList":[],"type":"MYSQL","sql":"/* ===========================================================\r\n   环境准备（按需修改库名）\r\n   =========================================================== */\r\n-- 可选：使用你的业务库\r\nCREATE DATABASE IF NOT EXISTS hydrology DEFAULT CHARACTER SET utf8mb4;\r\nUSE hydrology;\r\n\r\n/* ===========================================================\r\n   1) 报表表（日平均水位）\r\n   - 以 (station_code, stat_date) 做唯一键，兼容无维表场景\r\n   - 如存在维表，会同步写入 station_id / station_name\r\n   =========================================================== */\r\nCREATE TABLE IF NOT EXISTS dws_station_water_day_report (\r\n  station_code   VARCHAR(64)  NOT NULL COMMENT ''测站编码'',\r\n  station_id     BIGINT                COMMENT ''测站ID（有维表时填充）'',\r\n  station_name   VARCHAR(128)          COMMENT ''测站名称（有维表时填充；无维表时=station_code）'',\r\n  avg_level_day  DECIMAL(10,3)         COMMENT ''日平均水位(米)'',\r\n  stat_date      DATE         NOT NULL COMMENT ''统计日期'',\r\n  PRIMARY KEY (station_code, stat_date),\r\n  KEY idx_stat_date (stat_date),\r\n  KEY idx_station_id (station_id)\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''站点水位日统计（日均）'';\r\n\r\n/* ===========================================================\r\n   2) 单日报表过程\r\n   - 输入：p_stat_date（统计yyyy-MM-dd）\r\n   - 逻辑：\r\n       取 obs_date，否则取 DATE(obs_time) 作为统计日期\r\n       按 (station_code, stat_date) 聚合 AVG(water_level_m)\r\n       LEFT JOIN hyd_station（如存在）填充 id/name\r\n       用 ON DUPLICATE KEY 做 upsert\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report $$\r\nCREATE PROCEDURE prc_gen_water_day_report(IN p_stat_date DATE)\r\nBEGIN\r\n  /*\r\n    说明：\r\n    - 若不存在 hyd_station 表，LEFT JOIN 返回 NULL，不影响执行；\r\n      station_id 将为 NULL，station_name 用 station_code 兜底。\r\n    - hyd_water_level 建议在 hydrology 库中（按需改库名前缀）。\r\n  */\r\n  INSERT INTO dws_station_water_day_report\r\n  (\r\n    station_code, station_id, station_name, avg_level_day, stat_date\r\n  )\r\n  SELECT\r\n    wl.station_code                                                AS station_code,\r\n    s.station_id                                                   AS station_id,               -- 可能为 NULL\r\n    COALESCE(s.station_name, wl.station_code)                      AS station_name,             -- 兜底\r\n    ROUND(AVG(CAST(wl.water_level_m AS DECIMAL(10,3))), 3)         AS avg_level_day,\r\n    p_stat_date                                                    AS stat_date\r\n  FROM hydrology.hyd_water_level wl\r\n  LEFT JOIN hydrology.hyd_station s\r\n    ON s.station_code = wl.station_code\r\n  WHERE\r\n      (CASE\r\n         WHEN wl.obs_date IS NOT NULL THEN wl.obs_date\r\n         ELSE DATE(wl.obs_time)\r\n       END) = p_stat_date\r\n    AND wl.water_level_m IS NOT NULL\r\n  GROUP BY wl.station_code, s.station_id, COALESCE(s.station_name, wl.station_code)\r\n  ON DUPLICATE KEY UPDATE\r\n      station_id     = VALUES(station_id),\r\n      station_name   = VALUES(station_name),\r\n      avg_level_day  = VALUES(avg_level_day);\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   3) 区间报表过程（含首尾）\r\n   =========================================================== */\r\nDELIMITER $$\r\n\r\nDROP PROCEDURE IF EXISTS prc_gen_water_day_report_range $$\r\nCREATE PROCEDURE prc_gen_water_day_report_range(IN p_begin_date DATE, IN p_end_date DATE)\r\nBEGIN\r\n  DECLARE v_day DATE;\r\n\r\n  IF p_begin_date IS NULL OR p_end_date IS NULL THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起止日期不可为空'';\r\n  END IF;\r\n\r\n  IF p_begin_date > p_end_date THEN\r\n    SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''起始日期不能晚于截止日期'';\r\n  END IF;\r\n\r\n  SET v_day = p_begin_date;\r\n  WHILE v_day <= p_end_date DO\r\n    CALL prc_gen_water_day_report(v_day);\r\n    SET v_day = DATE_ADD(v_day, INTERVAL 1 DAY);\r\n  END WHILE;\r\nEND $$\r\nDELIMITER ;\r\n\r\n/* ===========================================================\r\n   4) 使用示例\r\n   =========================================================== */\r\n-- 生成某一天（例如 2025-01-01）\r\nCALL prc_gen_water_day_report(''2025-01-01'');\r\n\r\n-- 生成一段时间（例如 2025-01-01 ~ 2025-01-07）\r\nCALL prc_gen_water_day_report_range(''2025-01-01'', ''2025-01-07'');\r\n\r\n-- 校验\r\nSELECT *\r\nFROM dws_station_water_day_report\r\nORDER BY stat_date DESC, station_code\r\nLIMIT 20;\r\n\r\n/* ===========================================================\r\n   5) （可选）事件调度：每天 00:10 生成“昨日日报”\r\n   - 需要：SET GLOBAL event_scheduler = ON;\r\n   - 如不需要可删除此段\r\n   =========================================================== */\r\nSET GLOBAL event_scheduler = ON;\r\n\r\nCREATE EVENT IF NOT EXISTS evt_gen_water_day_report_yesterday\r\nON SCHEDULE EVERY 1 DAY\r\nSTARTS TIMESTAMP(CURRENT_DATE, ''00:10:00'')\r\nDO\r\n  CALL prc_gen_water_day_report(DATE_SUB(CURDATE(), INTERVAL 1 DAY));\r\n","sqlType":"0","preStatements":[],"postStatements":[],"displayRows":10,"datasources":{"password":"sfdjfFF#s2332","database":"hydrology","other":{},"port":3306,"connectType":"ORACLE_SERVICE_NAME","host":"mysql57","type":"MYSQL","userName":"root"},"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 1, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 16:08:54.111', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (167, '清洗_水位异常值处理-2025-09-22', 'SPARK', 0, 152658885655872, 2, 164, '清洗_水位异常值处理-2-20250930160545028', 152317790975712, 7, '2025-09-30 16:05:45.054', '2025-09-30 16:05:45.103', '2025-09-30 16:06:26.495', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/152317790975712/152658885689664_2/164/167', '/opt/dolphinscheduler/logs/20250930/152658885689664/2/164/167.log', 0, 0, 21020, '', '{"localParams":[],"rawScript":"","resourceList":[],"programType":"JAVA","mainClass":"tech.qiantong.qdata.spark.etl.EtlApplication","mainJar":{"resourceName":"file:/dolphinscheduler/default/resources/spark-jar/qdata-etl-3.8.8.jar"},"deployMode":"client","mainArgs":"eyJyZWFkZXIiOnsibm9kZU5hbWUiOiLooajovpPlhaXnu4Tku7YiLCJjb21wb25lbnRUeXBlIjoiMSIsInByb2plY3RDb2RlIjoxNTIzMTc3OTA5NzU3MTIsIm5vZGVDb2RlIjoiMTUyNjU4NDkxMjIzMzYwIiwicGFyYW1ldGVyIjp7ImRiTmFtZSI6IldBVEVSX1RQIiwiY29sdW1uIjpbIklEIiwiU1RBVElPTl9DT0RFIiwiT0JTX1RJTUUiLCJXQVRFUl9MRVZFTCIsIlFVQUxJVFlfRkxBRyIsIlRTIl0sImRiVHlwZSI6IkRNOCIsInJlYWRlclByb3BlcnR5Ijp7ImRhdGFzb3VyY2VDb25maWciOnsidXNlcm5hbWUiOiJXQVRFUl9UUCIsInBhc3N3b3JkIjoibG1PeHgrM3dEdTNvVU14UktBUjNMUT09IiwiZGJuYW1lIjoiV0FURVJfVFAifSwiZGJOYW1lIjoiV0FURVJfVFAiLCJkYlR5cGUiOiJETTgiLCJob3N0IjoiZG04LWRlbW8iLCJwYXNzd29yZCI6InMyTEtyNkxNUXhWRFRReCIsInBvcnQiOjUyMzYsInVzZXJuYW1lIjoiV0FURVJfVFAifSwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJkYXRlSW5jcmVtZW50Q29uZmlnIjp7ImxvZ2ljIjoiYW5kIiwiZGF0ZUZvcm1hdCI6Inl5eXktTU0tZGQiLCJjb2x1bW4iOltdfSwiaWRJbmNyZW1lbnRDb25maWciOnsiaW5jcmVtZW50U3RhcnQiOiIifSwiZGF0YXNvdXJjZUlkIjo4LCJ3aGVyZSI6IiIsImNvbm5lY3Rpb24iOnsiamRiY1VybCI6ImpkYmM6ZG06Ly9kbTgtZGVtbzo1MjM2P3NjaGVtYT1XQVRFUl9UUCIsInRhYmxlIjoiV0FURVJfTEVWRUwifSwiYmF0Y2hTaXplIjoiMTAyNCIsInJlYWRNb2RlVHlwZSI6IjEiLCJ1c2VybmFtZSI6IldBVEVSX1RQIn0sIm5vZGVWZXJzaW9uIjoyfSwid3JpdGVyIjp7Im5vZGVOYW1lIjoi6KGo6L6T5Ye657uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjkxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTMyNTI2NzIiLCJwYXJhbWV0ZXIiOnsicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJzZWxlY3RlZENvbHVtbnMiOltdLCJ3cml0ZXJQcm9wZXJ0eSI6eyJkYXRhc291cmNlQ29uZmlnIjp7InVzZXJuYW1lIjoiZHdkIiwicGFzc3dvcmQiOiJsbU94eCszd0R1M29VTXhSS0FSM0xRPT0iLCJkYm5hbWUiOiJkd2QifSwiZGJOYW1lIjoiZHdkIiwiZGJUeXBlIjoiRE04IiwiaG9zdCI6ImRtOC1kZW1vIiwicGFzc3dvcmQiOiJzMkxLcjZMTVF4VkRUUXgiLCJwb3J0Ijo1MjM2LCJ1c2VybmFtZSI6ImR3ZCJ9LCJkYk5hbWUiOiJkd2QiLCJkYXRhc291cmNlSWQiOjIsImNvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJkYlR5cGUiOiJETTgiLCJjb25uZWN0aW9uIjp7ImpkYmNVcmwiOiJqZGJjOmRtOi8vZG04LWRlbW86NTIzNj9zY2hlbWE9ZHdkIiwidGFibGUiOiJEV0RfV0FURVJfTEVWRUxfQ0xFQU5fT1VUTElFUiJ9LCJiYXRjaFNpemUiOiIxMDI0IiwidGFyZ2V0X2NvbHVtbiI6WyJJRCIsIlNUQVRJT05fQ09ERSIsIk9CU19USU1FIiwiV0FURVJfTEVWRUwiLCJRVUFMSVRZX0ZMQUciLCJUUyJdLCJ3cml0ZU1vZGVUeXBlIjoyLCJ1c2VybmFtZSI6ImR3ZCJ9LCJub2RlVmVyc2lvbiI6Mn0sImNvbmZpZyI6eyJyZXNvdXJjZVVybCI6Ii9kb2xwaGluc2NoZWR1bGVyLyIsInRhc2tJbmZvIjp7InRhc2tDb2RlIjoiMTUyNjU4NDkwMDU0OTc2IiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibmFtZSI6Iua4hea0l1/msLTkvY3lvILluLjlgLzlpITnkIYiLCJ0YXNrVmVyc2lvbiI6Mn0sInJhYmJpdG1xIjp7Imhvc3QiOiJyYWJiaXRtcSIsInBhc3N3b3JkIjoiRWpeaVVORkxwOU1Rb3VjMSIsInBvcnQiOjU2NzIsInVzZXJuYW1lIjoiYWRtaW4ifX0sInRyYW5zaXRpb24iOlt7Im5vZGVOYW1lIjoi6L2s5o2i57uE5Lu2IiwiY29tcG9uZW50VHlwZSI6IjMxIiwicHJvamVjdENvZGUiOjE1MjMxNzc5MDk3NTcxMiwibm9kZUNvZGUiOiIxNTI2NTg1OTE4NjMxMDQiLCJwYXJhbWV0ZXIiOnsidGFibGVGaWVsZHMiOlt7Im5hbWUiOiLlvILluLjlgLzliZTpmaQiLCJydWxlTmFtZSI6IuaVsOWAvOi+ueeVjOiwg+aVtCIsInJ1bGVDb2RlIjoiMDAxIiwic3RhdHVzIjoiMSIsIndoZXJlQ2xhdXNlIjoiIiwiY29sdW1ucyI6WyJXQVRFUl9MRVZFTCJdLCJ0YWJsZU5hbWUiOiIiLCJydWxlRGVzYyI6IiIsInR5cGUiOiIzIiwicnVsZUNvbmZpZyI6IntcImNvbHVtbnNcIjpbXCJXQVRFUl9MRVZFTFwiXSxcIm1pblwiOlwiMFwiLFwibWF4XCI6XCIxMDBcIixcImhhbmRsZVR5cGVcIjpcIjFcIixcInBhcmVudE5hbWVcIjpcIuW8guW4uOWAvOS/ruato+exu1wifSIsImlkIjoiIiwicGFyZW50TmFtZSI6IuW8guW4uOWAvOS/ruato+exuyIsInJ1bGVUeXBlIjoiV0lUSElOX0JPVU5EQVJZIn1dLCJ3aGVyZSI6IiJ9LCJub2RlVmVyc2lvbiI6Mn1dfQ==","master":"spark://spark:7077","driverCores":1,"driverMemory":"512m","numExecutors":1,"executorMemory":"512m","executorCores":1,"yarnQueue":"","sqlExecutionType":"SCRIPT","conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export
PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 16:05:45.054', 0, 0, '[]', 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (136, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 133, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250928110617544', 147372832245312, 6, '2025-09-28 11:06:17.859', '2025-09-28 11:06:18.472', '2025-09-28 11:06:18.925', '172.28.0.16:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/133/136', '/opt/dolphinscheduler/logs/20250928/152665378283840/1/133/136.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-28 11:06:17.859', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (137, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 134, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250928110617544', 147372832245312, 6, '2025-09-28 11:06:17.859', '2025-09-28 11:06:18.467', '2025-09-28 11:06:18.929', '172.28.0.16:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/134/137', '/opt/dolphinscheduler/logs/20250928/152665378283840/1/134/137.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-28 11:06:17.859', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (139, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 136, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250930093424358', 147372832245312, 6, '2025-09-30 09:34:24.636', '2025-09-30 09:34:25.259', '2025-09-30 09:34:25.672', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/136/139', '/opt/dolphinscheduler/logs/20250930/152665378283840/1/136/139.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 09:34:24.636', 0, 0, NULL, 0, -1, -1, 0);
INSERT INTO public.t_ds_task_instance VALUES (138, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868', 'HTTP', 0, 152665378277696, 1, 135, '水文监测水位数据（原始库）_ods_hyd_water_level_20250922160649aWZ6tCb1758528445868-1-20250930093424358', 147372832245312, 6, '2025-09-30 09:34:24.636', '2025-09-30 09:34:25.255', '2025-09-30 09:34:25.67', '172.28.0.17:1234', '/tmp/dolphinscheduler/exec/process/root/147372832245312/152665378283840_1/135/138', '/opt/dolphinscheduler/logs/20250930/152665378283840/1/135/138.log', 0, 0, 0, '', '{"condition":"","httpBody":"","httpCheckCondition":"STATUS_CODE_DEFAULT","httpParams":[{"prop":"id","value":8,"httpParametersType":"BODY"}],"connectTimeout":60000,"socketTimeout":60000,"localParams":[],"httpMethod":"PUT","url":"http://qdata-quality:8083/quality/qualityTaskExecutor/runExecuteTask/8","resourceList":[],"conditionResult":"null","dependence":"null","switchResult":"null","waitStartTimeout":null}', 1, 0, NULL, 0, 0, 2, 'default', 133155949418208, 'export FLINK_HOME=/opt/soft/flink
export SPARK_HOME=/opt/soft/spark
export DATAX_LAUNCHER=/opt/soft/datax/bin/datax.py
export PYTHON_LAUNCHER=/usr/bin/python3
export JAVA_HOME=/opt/java/openjdk
export PATH=$FLINK_HOME/bin:$SPARK_HOME/bin:$PYTHON_LAUNCHER:$JAVA_HOME/bin:$DATAX_LAUNCHER:$PATH', 1, 'admin', '2025-09-30 09:34:24.636', 0, 0, NULL, 0, -1, -1, 0);


--
-- Data for Name: t_ds_tenant; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_tenant VALUES (-1, 'default', 'default tenant', 1, '2018-03-27 15:48:50', '2018-10-24 17:40:22');


--
-- Data for Name: t_ds_trigger_relation; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_trigger_relation VALUES (1, 2, 150911801041120, 1, '2025-09-02 17:26:54.128', '2025-09-02 17:26:54.128');
INSERT INTO public.t_ds_trigger_relation VALUES (2, 0, 150911801041120, 1, '2025-09-02 17:26:55.494', '2025-09-02 17:26:55.494');
INSERT INTO public.t_ds_trigger_relation VALUES (3, 2, 150912070712544, 2, '2025-09-02 17:31:25.556', '2025-09-02 17:31:25.556');
INSERT INTO public.t_ds_trigger_relation VALUES (4, 0, 150912070712544, 2, '2025-09-02 17:31:26.143', '2025-09-02 17:31:26.143');
INSERT INTO public.t_ds_trigger_relation VALUES (5, 2, 150912311858400, 3, '2025-09-02 17:35:29.254', '2025-09-02 17:35:29.254');
INSERT INTO public.t_ds_trigger_relation VALUES (6, 0, 150912311858400, 3, '2025-09-02 17:35:29.873', '2025-09-02 17:35:29.873');
INSERT INTO public.t_ds_trigger_relation VALUES (7, 2, 150981042149120, 4, '2025-09-03 12:13:43.984', '2025-09-03 12:13:43.984');
INSERT INTO public.t_ds_trigger_relation VALUES (8, 0, 150981042149120, 4, '2025-09-03 12:13:44.533', '2025-09-03 12:13:44.533');
INSERT INTO public.t_ds_trigger_relation VALUES (9, 2, 150982637189888, 5, '2025-09-03 12:39:42.119', '2025-09-03 12:39:42.119');
INSERT INTO public.t_ds_trigger_relation VALUES (10, 0, 150982637189888, 5, '2025-09-03 12:39:42.558', '2025-09-03 12:39:42.558');
INSERT INTO public.t_ds_trigger_relation VALUES (11, 2, 151678342036128, 6, '2025-09-11 09:23:00.915', '2025-09-11 09:23:00.915');
INSERT INTO public.t_ds_trigger_relation VALUES (12, 0, 151678342036128, 6, '2025-09-11 09:23:01.964', '2025-09-11 09:23:01.964');
INSERT INTO public.t_ds_trigger_relation VALUES (13, 2, 151678423706272, 7, '2025-09-11 09:24:21.529', '2025-09-11 09:24:21.529');
INSERT INTO public.t_ds_trigger_relation VALUES (14, 0, 151678423706272, 7, '2025-09-11 09:24:22.115', '2025-09-11 09:24:22.115');
INSERT INTO public.t_ds_trigger_relation VALUES (15, 2, 151678565831328, 8, '2025-09-11 09:26:42.185', '2025-09-11 09:26:42.185');
INSERT INTO public.t_ds_trigger_relation VALUES (16, 0, 151678565831328, 8, '2025-09-11 09:26:42.236', '2025-09-11 09:26:42.236');
INSERT INTO public.t_ds_trigger_relation VALUES (17, 2, 151680683218592, 9, '2025-09-11 10:01:51.261', '2025-09-11 10:01:51.261');
INSERT INTO public.t_ds_trigger_relation VALUES (18, 0, 151680683218592, 9, '2025-09-11 10:01:52.141', '2025-09-11 10:01:52.141');
INSERT INTO public.t_ds_trigger_relation VALUES (19, 2, 151680924559008, 10, '2025-09-11 10:05:52.782', '2025-09-11 10:05:52.782');
INSERT INTO public.t_ds_trigger_relation VALUES (20, 0, 151680924559008, 10, '2025-09-11 10:05:53.462', '2025-09-11 10:05:53.462');
INSERT INTO public.t_ds_trigger_relation VALUES (21, 2, 151680993317536, 11, '2025-09-11 10:07:01.365', '2025-09-11 10:07:01.365');
INSERT INTO public.t_ds_trigger_relation VALUES (22, 0, 151680993317536, 11, '2025-09-11 10:07:02.065', '2025-09-11 10:07:02.065');
INSERT INTO public.t_ds_trigger_relation VALUES (23, 2, 152149761155008, 12, '2025-09-16 17:15:51.144', '2025-09-16 17:15:51.144');
INSERT INTO public.t_ds_trigger_relation VALUES (24, 0, 152149761155008, 12, '2025-09-16 17:15:52.371', '2025-09-16 17:15:52.371');
INSERT INTO public.t_ds_trigger_relation VALUES (25, 2, 152150123718592, 13, '2025-09-16 17:21:45.206', '2025-09-16 17:21:45.206');
INSERT INTO public.t_ds_trigger_relation VALUES (26, 0, 152150123718592, 13, '2025-09-16 17:21:46.152', '2025-09-16 17:21:46.152');
INSERT INTO public.t_ds_trigger_relation VALUES (27, 2, 152150302072768, 14, '2025-09-16 17:24:39.371', '2025-09-16 17:24:39.371');
INSERT INTO public.t_ds_trigger_relation VALUES (28, 0, 152150302072768, 14, '2025-09-16 17:24:39.619', '2025-09-16 17:24:39.619');
INSERT INTO public.t_ds_trigger_relation VALUES (29, 2, 152150352055232, 15, '2025-09-16 17:25:28.18', '2025-09-16 17:25:28.18');
INSERT INTO public.t_ds_trigger_relation VALUES (30, 0, 152150352055232, 15, '2025-09-16 17:25:28.727', '2025-09-16 17:25:28.727');
INSERT INTO public.t_ds_trigger_relation VALUES (31, 2, 152150449330112, 16, '2025-09-16 17:27:03.176', '2025-09-16 17:27:03.176');
INSERT INTO public.t_ds_trigger_relation VALUES (32, 0, 152150449330112, 16, '2025-09-16 17:27:03.902', '2025-09-16 17:27:03.902');
INSERT INTO public.t_ds_trigger_relation VALUES (33, 2, 152150539101120, 17, '2025-09-16 17:28:30.843', '2025-09-16 17:28:30.843');
INSERT INTO public.t_ds_trigger_relation VALUES (34, 0, 152150539101120, 17, '2025-09-16 17:28:31.07', '2025-09-16 17:28:31.07');
INSERT INTO public.t_ds_trigger_relation VALUES (35, 2, 152150624321472, 18, '2025-09-16 17:29:54.067', '2025-09-16 17:29:54.067');
INSERT INTO public.t_ds_trigger_relation VALUES (36, 0, 152150624321472, 18, '2025-09-16 17:29:54.233', '2025-09-16 17:29:54.233');
INSERT INTO public.t_ds_trigger_relation VALUES (37, 2, 152150626800576, 19, '2025-09-16 17:29:56.487', '2025-09-16 17:29:56.487');
INSERT INTO public.t_ds_trigger_relation VALUES (38, 0, 152150626800576, 19, '2025-09-16 17:29:57.248', '2025-09-16 17:29:57.248');
INSERT INTO public.t_ds_trigger_relation VALUES (39, 2, 152150634215360, 20, '2025-09-16 17:30:03.728', '2025-09-16 17:30:03.728');
INSERT INTO public.t_ds_trigger_relation VALUES (40, 0, 152150634215360, 20, '2025-09-16 17:30:04.273', '2025-09-16 17:30:04.273');
INSERT INTO public.t_ds_trigger_relation VALUES (41, 2, 152413382355264, 21, '2025-09-19 16:46:33.722', '2025-09-19 16:46:33.722');
INSERT INTO public.t_ds_trigger_relation VALUES (42, 0, 152413382355264, 21, '2025-09-19 16:46:35.078', '2025-09-19 16:46:35.078');
INSERT INTO public.t_ds_trigger_relation VALUES (43, 2, 152413559229760, 22, '2025-09-19 16:49:36.833', '2025-09-19 16:49:36.833');
INSERT INTO public.t_ds_trigger_relation VALUES (44, 0, 152413559229760, 22, '2025-09-19 16:49:37.799', '2025-09-19 16:49:37.799');
INSERT INTO public.t_ds_trigger_relation VALUES (45, 2, 152413562231104, 23, '2025-09-19 16:49:39.762', '2025-09-19 16:49:39.762');
INSERT INTO public.t_ds_trigger_relation VALUES (46, 0, 152413562231104, 23, '2025-09-19 16:49:39.816', '2025-09-19 16:49:39.816');
INSERT INTO public.t_ds_trigger_relation VALUES (47, 2, 152413564720448, 24, '2025-09-19 16:49:42.194', '2025-09-19 16:49:42.194');
INSERT INTO public.t_ds_trigger_relation VALUES (48, 0, 152413564720448, 24, '2025-09-19 16:49:42.832', '2025-09-19 16:49:42.832');
INSERT INTO public.t_ds_trigger_relation VALUES (49, 2, 152413581377856, 25, '2025-09-19 16:49:58.459', '2025-09-19 16:49:58.459');
INSERT INTO public.t_ds_trigger_relation VALUES (50, 0, 152413581377856, 25, '2025-09-19 16:49:58.876', '2025-09-19 16:49:58.876');
INSERT INTO public.t_ds_trigger_relation VALUES (51, 2, 152414756863296, 26, '2025-09-19 17:10:35.436', '2025-09-19 17:10:35.436');
INSERT INTO public.t_ds_trigger_relation VALUES (52, 0, 152414756863296, 26, '2025-09-19 17:10:35.971', '2025-09-19 17:10:35.971');
INSERT INTO public.t_ds_trigger_relation VALUES (53, 2, 152414793923904, 27, '2025-09-19 17:11:14.271', '2025-09-19 17:11:14.271');
INSERT INTO public.t_ds_trigger_relation VALUES (54, 0, 152414793923904, 27, '2025-09-19 17:11:14.7', '2025-09-19 17:11:14.7');
INSERT INTO public.t_ds_trigger_relation VALUES (55, 2, 152416174995776, 28, '2025-09-19 17:35:18.832', '2025-09-19 17:35:18.832');
INSERT INTO public.t_ds_trigger_relation VALUES (56, 0, 152416174995776, 28, '2025-09-19 17:35:19.649', '2025-09-19 17:35:19.649');
INSERT INTO public.t_ds_trigger_relation VALUES (57, 2, 152416176459072, 29, '2025-09-19 17:35:20.26', '2025-09-19 17:35:20.26');
INSERT INTO public.t_ds_trigger_relation VALUES (58, 0, 152416176459072, 29, '2025-09-19 17:35:20.673', '2025-09-19 17:35:20.673');
INSERT INTO public.t_ds_trigger_relation VALUES (59, 2, 152439172286784, 30, '2025-09-20 00:09:41.496', '2025-09-20 00:09:41.496');
INSERT INTO public.t_ds_trigger_relation VALUES (60, 0, 152439172286784, 30, '2025-09-20 00:09:42.094', '2025-09-20 00:09:42.094');
INSERT INTO public.t_ds_trigger_relation VALUES (61, 2, 152439396480320, 31, '2025-09-20 00:13:20.425', '2025-09-20 00:13:20.425');
INSERT INTO public.t_ds_trigger_relation VALUES (62, 0, 152439396480320, 31, '2025-09-20 00:13:20.618', '2025-09-20 00:13:20.618');
INSERT INTO public.t_ds_trigger_relation VALUES (63, 2, 152439782159680, 32, '2025-09-20 00:19:37.066', '2025-09-20 00:19:37.066');
INSERT INTO public.t_ds_trigger_relation VALUES (64, 0, 152439782159680, 32, '2025-09-20 00:19:37.19', '2025-09-20 00:19:37.19');
INSERT INTO public.t_ds_trigger_relation VALUES (65, 2, 152439923053888, 33, '2025-09-20 00:21:54.656', '2025-09-20 00:21:54.656');
INSERT INTO public.t_ds_trigger_relation VALUES (66, 0, 152439923053888, 33, '2025-09-20 00:21:55.421', '2025-09-20 00:21:55.421');
INSERT INTO public.t_ds_trigger_relation VALUES (67, 2, 152439965486400, 34, '2025-09-20 00:22:36.096', '2025-09-20 00:22:36.096');
INSERT INTO public.t_ds_trigger_relation VALUES (68, 0, 152439965486400, 34, '2025-09-20 00:22:36.53', '2025-09-20 00:22:36.53');
INSERT INTO public.t_ds_trigger_relation VALUES (69, 2, 152440052280640, 35, '2025-09-20 00:24:00.855', '2025-09-20 00:24:00.855');
INSERT INTO public.t_ds_trigger_relation VALUES (70, 0, 152440052280640, 35, '2025-09-20 00:24:01.713', '2025-09-20 00:24:01.713');
INSERT INTO public.t_ds_trigger_relation VALUES (71, 2, 152642488077632, 36, '2025-09-22 09:27:29.392', '2025-09-22 09:27:29.392');
INSERT INTO public.t_ds_trigger_relation VALUES (72, 0, 152642488077632, 36, '2025-09-22 09:27:29.892', '2025-09-22 09:27:29.892');
INSERT INTO public.t_ds_trigger_relation VALUES (73, 2, 152648207127872, 37, '2025-09-22 11:06:56.329', '2025-09-22 11:06:56.329');
INSERT INTO public.t_ds_trigger_relation VALUES (74, 0, 152648207127872, 37, '2025-09-22 11:06:56.985', '2025-09-22 11:06:56.985');
INSERT INTO public.t_ds_trigger_relation VALUES (75, 2, 152648832380224, 38, '2025-09-22 11:17:51.365', '2025-09-22 11:17:51.365');
INSERT INTO public.t_ds_trigger_relation VALUES (76, 0, 152648832380224, 38, '2025-09-22 11:17:52.292', '2025-09-22 11:17:52.292');
INSERT INTO public.t_ds_trigger_relation VALUES (77, 2, 152648932504896, 39, '2025-09-22 11:19:36.388', '2025-09-22 11:19:36.388');
INSERT INTO public.t_ds_trigger_relation VALUES (78, 0, 152648932504896, 39, '2025-09-22 11:19:36.731', '2025-09-22 11:19:36.731');
INSERT INTO public.t_ds_trigger_relation VALUES (79, 2, 152649939519808, 40, '2025-09-22 11:37:21.061', '2025-09-22 11:37:21.061');
INSERT INTO public.t_ds_trigger_relation VALUES (80, 0, 152649939519808, 40, '2025-09-22 11:37:21.607', '2025-09-22 11:37:21.607');
INSERT INTO public.t_ds_trigger_relation VALUES (81, 2, 152651432419648, 41, '2025-09-22 12:03:11.631', '2025-09-22 12:03:11.631');
INSERT INTO public.t_ds_trigger_relation VALUES (82, 0, 152651432419648, 41, '2025-09-22 12:03:12.347', '2025-09-22 12:03:12.347');
INSERT INTO public.t_ds_trigger_relation VALUES (83, 2, 152651436516672, 42, '2025-09-22 12:03:15.628', '2025-09-22 12:03:15.628');
INSERT INTO public.t_ds_trigger_relation VALUES (84, 0, 152651436516672, 42, '2025-09-22 12:03:16.372', '2025-09-22 12:03:16.372');
INSERT INTO public.t_ds_trigger_relation VALUES (85, 2, 152651439255872, 43, '2025-09-22 12:03:18.304', '2025-09-22 12:03:18.304');
INSERT INTO public.t_ds_trigger_relation VALUES (86, 0, 152651439255872, 43, '2025-09-22 12:03:18.385', '2025-09-22 12:03:18.385');
INSERT INTO public.t_ds_trigger_relation VALUES (87, 2, 152651441750336, 44, '2025-09-22 12:03:20.739', '2025-09-22 12:03:20.739');
INSERT INTO public.t_ds_trigger_relation VALUES (88, 0, 152651441750336, 44, '2025-09-22 12:03:21.404', '2025-09-22 12:03:21.404');
INSERT INTO public.t_ds_trigger_relation VALUES (89, 2, 152653941697856, 45, '2025-09-22 12:46:53.871', '2025-09-22 12:46:53.871');
INSERT INTO public.t_ds_trigger_relation VALUES (90, 0, 152653941697856, 45, '2025-09-22 12:46:53.99', '2025-09-22 12:46:53.99');
INSERT INTO public.t_ds_trigger_relation VALUES (91, 2, 152655005475136, 46, '2025-09-22 13:05:49.706', '2025-09-22 13:05:49.706');
INSERT INTO public.t_ds_trigger_relation VALUES (92, 0, 152655005475136, 46, '2025-09-22 13:05:49.892', '2025-09-22 13:05:49.892');
INSERT INTO public.t_ds_trigger_relation VALUES (93, 2, 152655301501248, 47, '2025-09-22 13:10:53.844', '2025-09-22 13:10:53.844');
INSERT INTO public.t_ds_trigger_relation VALUES (94, 0, 152655301501248, 47, '2025-09-22 13:10:54.516', '2025-09-22 13:10:54.516');
INSERT INTO public.t_ds_trigger_relation VALUES (95, 2, 152655506918720, 48, '2025-09-22 13:14:28.943', '2025-09-22 13:14:28.943');
INSERT INTO public.t_ds_trigger_relation VALUES (96, 0, 152655506918720, 48, '2025-09-22 13:14:29.416', '2025-09-22 13:14:29.416');
INSERT INTO public.t_ds_trigger_relation VALUES (97, 2, 152656052340032, 49, '2025-09-22 13:23:51.614', '2025-09-22 13:23:51.614');
INSERT INTO public.t_ds_trigger_relation VALUES (98, 0, 152656052340032, 49, '2025-09-22 13:23:52.286', '2025-09-22 13:23:52.286');
INSERT INTO public.t_ds_trigger_relation VALUES (99, 2, 152656282161472, 50, '2025-09-22 13:27:48.954', '2025-09-22 13:27:48.954');
INSERT INTO public.t_ds_trigger_relation VALUES (100, 0, 152656282161472, 50, '2025-09-22 13:27:49.605', '2025-09-22 13:27:49.605');
INSERT INTO public.t_ds_trigger_relation VALUES (101, 2, 152656669109568, 51, '2025-09-22 13:34:32.024', '2025-09-22 13:34:32.024');
INSERT INTO public.t_ds_trigger_relation VALUES (102, 0, 152656669109568, 51, '2025-09-22 13:34:32.561', '2025-09-22 13:34:32.561');
INSERT INTO public.t_ds_trigger_relation VALUES (103, 2, 152657241276736, 52, '2025-09-22 13:44:36.137', '2025-09-22 13:44:36.137');
INSERT INTO public.t_ds_trigger_relation VALUES (104, 0, 152657241276736, 52, '2025-09-22 13:44:37.014', '2025-09-22 13:44:37.014');
INSERT INTO public.t_ds_trigger_relation VALUES (105, 2, 152657660162368, 53, '2025-09-22 13:51:59.28', '2025-09-22 13:51:59.28');
INSERT INTO public.t_ds_trigger_relation VALUES (106, 0, 152657660162368, 53, '2025-09-22 13:51:59.837', '2025-09-22 13:51:59.837');
INSERT INTO public.t_ds_trigger_relation VALUES (107, 2, 152657723477312, 54, '2025-09-22 13:53:06.142', '2025-09-22 13:53:06.142');
INSERT INTO public.t_ds_trigger_relation VALUES (108, 0, 152657723477312, 54, '2025-09-22 13:53:07.106', '2025-09-22 13:53:07.106');
INSERT INTO public.t_ds_trigger_relation VALUES (109, 2, 152657964428608, 55, '2025-09-22 13:57:18.696', '2025-09-22 13:57:18.696');
INSERT INTO public.t_ds_trigger_relation VALUES (110, 0, 152657964428608, 55, '2025-09-22 13:57:19.286', '2025-09-22 13:57:19.286');
INSERT INTO public.t_ds_trigger_relation VALUES (111, 2, 152658268980544, 56, '2025-09-22 14:02:44.876', '2025-09-22 14:02:44.876');
INSERT INTO public.t_ds_trigger_relation VALUES (112, 0, 152658268980544, 56, '2025-09-22 14:02:45.844', '2025-09-22 14:02:45.844');
INSERT INTO public.t_ds_trigger_relation VALUES (113, 2, 152658890665280, 57, '2025-09-22 14:13:41.159', '2025-09-22 14:13:41.159');
INSERT INTO public.t_ds_trigger_relation VALUES (114, 0, 152658890665280, 57, '2025-09-22 14:13:41.377', '2025-09-22 14:13:41.377');
INSERT INTO public.t_ds_trigger_relation VALUES (115, 2, 152659124712768, 58, '2025-09-22 14:17:50.136', '2025-09-22 14:17:50.136');
INSERT INTO public.t_ds_trigger_relation VALUES (116, 0, 152659124712768, 58, '2025-09-22 14:17:50.186', '2025-09-22 14:17:50.186');
INSERT INTO public.t_ds_trigger_relation VALUES (117, 2, 152659244480832, 59, '2025-09-22 14:19:55.777', '2025-09-22 14:19:55.777');
INSERT INTO public.t_ds_trigger_relation VALUES (118, 0, 152659244480832, 59, '2025-09-22 14:19:56.084', '2025-09-22 14:19:56.084');
INSERT INTO public.t_ds_trigger_relation VALUES (119, 2, 152659305989440, 60, '2025-09-22 14:20:57.625', '2025-09-22 14:20:57.625');
INSERT INTO public.t_ds_trigger_relation VALUES (120, 0, 152659305989440, 60, '2025-09-22 14:20:58.046', '2025-09-22 14:20:58.046');
INSERT INTO public.t_ds_trigger_relation VALUES (121, 2, 152660162199872, 61, '2025-09-22 14:35:47.361', '2025-09-22 14:35:47.361');
INSERT INTO public.t_ds_trigger_relation VALUES (122, 0, 152660162199872, 61, '2025-09-22 14:35:48.108', '2025-09-22 14:35:48.108');
INSERT INTO public.t_ds_trigger_relation VALUES (123, 2, 152660637059392, 62, '2025-09-22 14:43:59.337', '2025-09-22 14:43:59.337');
INSERT INTO public.t_ds_trigger_relation VALUES (124, 0, 152660637059392, 62, '2025-09-22 14:44:00.09', '2025-09-22 14:44:00.09');
INSERT INTO public.t_ds_trigger_relation VALUES (125, 2, 152660732485952, 63, '2025-09-22 14:45:36.648', '2025-09-22 14:45:36.648');
INSERT INTO public.t_ds_trigger_relation VALUES (126, 0, 152660732485952, 63, '2025-09-22 14:45:37.371', '2025-09-22 14:45:37.371');
INSERT INTO public.t_ds_trigger_relation VALUES (127, 2, 152660868955456, 64, '2025-09-22 14:47:57.697', '2025-09-22 14:47:57.697');
INSERT INTO public.t_ds_trigger_relation VALUES (128, 0, 152660868955456, 64, '2025-09-22 14:47:58.372', '2025-09-22 14:47:58.372');
INSERT INTO public.t_ds_trigger_relation VALUES (129, 2, 152661517158720, 65, '2025-09-22 14:59:15.684', '2025-09-22 14:59:15.684');
INSERT INTO public.t_ds_trigger_relation VALUES (130, 0, 152661517158720, 65, '2025-09-22 14:59:16.397', '2025-09-22 14:59:16.397');
INSERT INTO public.t_ds_trigger_relation VALUES (131, 2, 152661967545664, 66, '2025-09-22 15:07:05.171', '2025-09-22 15:07:05.171');
INSERT INTO public.t_ds_trigger_relation VALUES (132, 0, 152661967545664, 66, '2025-09-22 15:07:05.869', '2025-09-22 15:07:05.869');
INSERT INTO public.t_ds_trigger_relation VALUES (133, 2, 152662779928896, 67, '2025-09-22 15:21:23.814', '2025-09-22 15:21:23.814');
INSERT INTO public.t_ds_trigger_relation VALUES (134, 0, 152662779928896, 67, '2025-09-22 15:21:24.635', '2025-09-22 15:21:24.635');
INSERT INTO public.t_ds_trigger_relation VALUES (135, 2, 152662819697984, 68, '2025-09-22 15:22:05.179', '2025-09-22 15:22:05.179');
INSERT INTO public.t_ds_trigger_relation VALUES (136, 0, 152662819697984, 68, '2025-09-22 15:22:05.228', '2025-09-22 15:22:05.228');
INSERT INTO public.t_ds_trigger_relation VALUES (137, 2, 152662870463808, 69, '2025-09-22 15:22:59.85', '2025-09-22 15:22:59.85');
INSERT INTO public.t_ds_trigger_relation VALUES (138, 0, 152662870463808, 69, '2025-09-22 15:23:00.407', '2025-09-22 15:23:00.407');
INSERT INTO public.t_ds_trigger_relation VALUES (139, 2, 152662940116288, 70, '2025-09-22 15:24:13.185', '2025-09-22 15:24:13.185');
INSERT INTO public.t_ds_trigger_relation VALUES (140, 0, 152662940116288, 70, '2025-09-22 15:24:13.828', '2025-09-22 15:24:13.828');
INSERT INTO public.t_ds_trigger_relation VALUES (141, 2, 152662979561792, 71, '2025-09-22 15:24:54.486', '2025-09-22 15:24:54.486');
INSERT INTO public.t_ds_trigger_relation VALUES (142, 0, 152662979561792, 71, '2025-09-22 15:24:54.682', '2025-09-22 15:24:54.682');
INSERT INTO public.t_ds_trigger_relation VALUES (143, 2, 152662998035776, 72, '2025-09-22 15:25:12.527', '2025-09-22 15:25:12.527');
INSERT INTO public.t_ds_trigger_relation VALUES (144, 0, 152662998035776, 72, '2025-09-22 15:25:12.718', '2025-09-22 15:25:12.718');
INSERT INTO public.t_ds_trigger_relation VALUES (145, 2, 152663111964992, 73, '2025-09-22 15:27:14.821', '2025-09-22 15:27:14.821');
INSERT INTO public.t_ds_trigger_relation VALUES (146, 0, 152663111964992, 73, '2025-09-22 15:27:14.933', '2025-09-22 15:27:14.933');
INSERT INTO public.t_ds_trigger_relation VALUES (147, 2, 152663502451008, 74, '2025-09-22 15:34:11.45', '2025-09-22 15:34:11.45');
INSERT INTO public.t_ds_trigger_relation VALUES (148, 0, 152663502451008, 74, '2025-09-22 15:34:11.812', '2025-09-22 15:34:11.812');
INSERT INTO public.t_ds_trigger_relation VALUES (149, 2, 152663689968960, 75, '2025-09-22 15:37:27.911', '2025-09-22 15:37:27.911');
INSERT INTO public.t_ds_trigger_relation VALUES (150, 0, 152663689968960, 75, '2025-09-22 15:37:28.41', '2025-09-22 15:37:28.41');
INSERT INTO public.t_ds_trigger_relation VALUES (151, 2, 152663907699008, 76, '2025-09-22 15:41:16.799', '2025-09-22 15:41:16.799');
INSERT INTO public.t_ds_trigger_relation VALUES (152, 0, 152663907699008, 76, '2025-09-22 15:41:17.044', '2025-09-22 15:41:17.044');
INSERT INTO public.t_ds_trigger_relation VALUES (153, 2, 152663967366464, 77, '2025-09-22 15:42:21.634', '2025-09-22 15:42:21.634');
INSERT INTO public.t_ds_trigger_relation VALUES (154, 0, 152663967366464, 77, '2025-09-22 15:42:21.725', '2025-09-22 15:42:21.725');
INSERT INTO public.t_ds_trigger_relation VALUES (155, 2, 152664053290304, 78, '2025-09-22 15:43:54.497', '2025-09-22 15:43:54.497');
INSERT INTO public.t_ds_trigger_relation VALUES (156, 0, 152664053290304, 78, '2025-09-22 15:43:54.808', '2025-09-22 15:43:54.808');
INSERT INTO public.t_ds_trigger_relation VALUES (157, 2, 152664876808512, 79, '2025-09-22 15:58:31.28', '2025-09-22 15:58:31.28');
INSERT INTO public.t_ds_trigger_relation VALUES (158, 0, 152664876808512, 79, '2025-09-22 15:58:31.627', '2025-09-22 15:58:31.627');
INSERT INTO public.t_ds_trigger_relation VALUES (159, 2, 152665062893888, 80, '2025-09-22 16:01:48.797', '2025-09-22 16:01:48.797');
INSERT INTO public.t_ds_trigger_relation VALUES (160, 0, 152665062893888, 80, '2025-09-22 16:01:49.729', '2025-09-22 16:01:49.729');
INSERT INTO public.t_ds_trigger_relation VALUES (161, 2, 152665322740032, 81, '2025-09-22 16:06:25.953', '2025-09-22 16:06:25.953');
INSERT INTO public.t_ds_trigger_relation VALUES (162, 0, 152665322740032, 81, '2025-09-22 16:06:26.477', '2025-09-22 16:06:26.477');
INSERT INTO public.t_ds_trigger_relation VALUES (163, 2, 152665380548928, 82, '2025-09-22 16:07:28.09', '2025-09-22 16:07:28.09');
INSERT INTO public.t_ds_trigger_relation VALUES (164, 0, 152665380548928, 82, '2025-09-22 16:07:28.248', '2025-09-22 16:07:28.248');
INSERT INTO public.t_ds_trigger_relation VALUES (165, 2, 152665434924352, 83, '2025-09-22 16:08:24.063', '2025-09-22 16:08:24.063');
INSERT INTO public.t_ds_trigger_relation VALUES (166, 0, 152665434924352, 83, '2025-09-22 16:08:24.202', '2025-09-22 16:08:24.202');
INSERT INTO public.t_ds_trigger_relation VALUES (167, 2, 152665565667648, 84, '2025-09-22 16:10:44.98', '2025-09-22 16:10:44.98');
INSERT INTO public.t_ds_trigger_relation VALUES (168, 0, 152665565667648, 84, '2025-09-22 16:10:45.64', '2025-09-22 16:10:45.64');
INSERT INTO public.t_ds_trigger_relation VALUES (169, 2, 152665775064384, 85, '2025-09-22 16:14:27.704', '2025-09-22 16:14:27.704');
INSERT INTO public.t_ds_trigger_relation VALUES (170, 0, 152665775064384, 85, '2025-09-22 16:14:28.309', '2025-09-22 16:14:28.309');
INSERT INTO public.t_ds_trigger_relation VALUES (171, 2, 152666037350720, 87, '2025-09-22 16:19:03.449', '2025-09-22 16:19:03.449');
INSERT INTO public.t_ds_trigger_relation VALUES (172, 0, 152666037350720, 86, '2025-09-22 16:19:04.419', '2025-09-22 16:19:04.419');
INSERT INTO public.t_ds_trigger_relation VALUES (173, 2, 152666637919552, 88, '2025-09-22 16:29:13.681', '2025-09-22 16:29:13.681');
INSERT INTO public.t_ds_trigger_relation VALUES (174, 0, 152666637919552, 87, '2025-09-22 16:29:14.126', '2025-09-22 16:29:14.126');
INSERT INTO public.t_ds_trigger_relation VALUES (175, 2, 152666819859776, 89, '2025-09-22 16:32:13.868', '2025-09-22 16:32:13.868');
INSERT INTO public.t_ds_trigger_relation VALUES (176, 0, 152666819859776, 88, '2025-09-22 16:32:13.95', '2025-09-22 16:32:13.95');
INSERT INTO public.t_ds_trigger_relation VALUES (177, 2, 152666864125248, 90, '2025-09-22 16:32:57.994', '2025-09-22 16:32:57.994');
INSERT INTO public.t_ds_trigger_relation VALUES (178, 0, 152666864125248, 89, '2025-09-22 16:32:58.937', '2025-09-22 16:32:58.937');
INSERT INTO public.t_ds_trigger_relation VALUES (179, 2, 152666928357696, 91, '2025-09-22 16:34:01.201', '2025-09-22 16:34:01.201');
INSERT INTO public.t_ds_trigger_relation VALUES (180, 0, 152666928357696, 90, '2025-09-22 16:34:01.528', '2025-09-22 16:34:01.528');
INSERT INTO public.t_ds_trigger_relation VALUES (181, 2, 152667130023232, 92, '2025-09-22 16:37:18.143', '2025-09-22 16:37:18.143');
INSERT INTO public.t_ds_trigger_relation VALUES (182, 0, 152667130023232, 91, '2025-09-22 16:37:18.878', '2025-09-22 16:37:18.878');
INSERT INTO public.t_ds_trigger_relation VALUES (183, 2, 152667584108864, 93, '2025-09-22 16:44:41.589', '2025-09-22 16:44:41.589');
INSERT INTO public.t_ds_trigger_relation VALUES (184, 0, 152667584108864, 92, '2025-09-22 16:44:42.604', '2025-09-22 16:44:42.604');
INSERT INTO public.t_ds_trigger_relation VALUES (185, 2, 152668155475264, 94, '2025-09-22 16:53:59.559', '2025-09-22 16:53:59.559');
INSERT INTO public.t_ds_trigger_relation VALUES (186, 0, 152668155475264, 93, '2025-09-22 16:54:00.563', '2025-09-22 16:54:00.563');
INSERT INTO public.t_ds_trigger_relation VALUES (187, 2, 152668671083840, 95, '2025-09-22 17:02:23.085', '2025-09-22 17:02:23.085');
INSERT INTO public.t_ds_trigger_relation VALUES (188, 0, 152668671083840, 94, '2025-09-22 17:02:23.401', '2025-09-22 17:02:23.401');
INSERT INTO public.t_ds_trigger_relation VALUES (189, 2, 152668753885504, 96, '2025-09-22 17:03:43.944', '2025-09-22 17:03:43.944');
INSERT INTO public.t_ds_trigger_relation VALUES (190, 0, 152668753885504, 95, '2025-09-22 17:03:44.537', '2025-09-22 17:03:44.537');
INSERT INTO public.t_ds_trigger_relation VALUES (191, 2, 152669658680640, 97, '2025-09-22 17:18:27.535', '2025-09-22 17:18:27.535');
INSERT INTO public.t_ds_trigger_relation VALUES (192, 0, 152669658680640, 96, '2025-09-22 17:18:28.454', '2025-09-22 17:18:28.454');
INSERT INTO public.t_ds_trigger_relation VALUES (193, 2, 152670117856576, 98, '2025-09-22 17:25:55.946', '2025-09-22 17:25:55.946');
INSERT INTO public.t_ds_trigger_relation VALUES (194, 0, 152670117856576, 97, '2025-09-22 17:25:56.221', '2025-09-22 17:25:56.221');
INSERT INTO public.t_ds_trigger_relation VALUES (195, 2, 152670192651584, 99, '2025-09-22 17:27:08.989', '2025-09-22 17:27:08.989');
INSERT INTO public.t_ds_trigger_relation VALUES (196, 0, 152670192651584, 98, '2025-09-22 17:27:09.354', '2025-09-22 17:27:09.354');
INSERT INTO public.t_ds_trigger_relation VALUES (197, 2, 152670514827584, 100, '2025-09-22 17:32:23.614', '2025-09-22 17:32:23.614');
INSERT INTO public.t_ds_trigger_relation VALUES (198, 0, 152670514827584, 99, '2025-09-22 17:32:23.959', '2025-09-22 17:32:23.959');
INSERT INTO public.t_ds_trigger_relation VALUES (199, 2, 152670686674240, 101, '2025-09-22 17:35:11.433', '2025-09-22 17:35:11.433');
INSERT INTO public.t_ds_trigger_relation VALUES (200, 0, 152670686674240, 100, '2025-09-22 17:35:12.398', '2025-09-22 17:35:12.398');
INSERT INTO public.t_ds_trigger_relation VALUES (201, 2, 152671214405952, 102, '2025-09-22 17:43:46.797', '2025-09-22 17:43:46.797');
INSERT INTO public.t_ds_trigger_relation VALUES (202, 0, 152671214405952, 101, '2025-09-22 17:43:47.626', '2025-09-22 17:43:47.626');
INSERT INTO public.t_ds_trigger_relation VALUES (203, 2, 152730329242944, 104, '2025-09-23 10:10:25.002', '2025-09-23 10:10:25.002');
INSERT INTO public.t_ds_trigger_relation VALUES (204, 0, 152730329242944, 103, '2025-09-23 10:10:26.217', '2025-09-23 10:10:26.217');
INSERT INTO public.t_ds_trigger_relation VALUES (205, 2, 152734935783744, 108, '2025-09-23 11:28:48.152', '2025-09-23 11:28:48.152');
INSERT INTO public.t_ds_trigger_relation VALUES (206, 0, 152734935783744, 107, '2025-09-23 11:28:49.047', '2025-09-23 11:28:49.047');
INSERT INTO public.t_ds_trigger_relation VALUES (207, 2, 152734938736960, 109, '2025-09-23 11:28:51.021', '2025-09-23 11:28:51.021');
INSERT INTO public.t_ds_trigger_relation VALUES (208, 0, 152734938736960, 108, '2025-09-23 11:28:51.125', '2025-09-23 11:28:51.125');
INSERT INTO public.t_ds_trigger_relation VALUES (209, 2, 152734940804416, 110, '2025-09-23 11:28:53.04', '2025-09-23 11:28:53.04');
INSERT INTO public.t_ds_trigger_relation VALUES (210, 0, 152734940804416, 109, '2025-09-23 11:28:53.136', '2025-09-23 11:28:53.136');
INSERT INTO public.t_ds_trigger_relation VALUES (211, 2, 152734942684480, 111, '2025-09-23 11:28:54.876', '2025-09-23 11:28:54.876');
INSERT INTO public.t_ds_trigger_relation VALUES (212, 0, 152734942684480, 110, '2025-09-23 11:28:55.15', '2025-09-23 11:28:55.15');
INSERT INTO public.t_ds_trigger_relation VALUES (213, 2, 152734945705280, 112, '2025-09-23 11:28:57.827', '2025-09-23 11:28:57.827');
INSERT INTO public.t_ds_trigger_relation VALUES (214, 0, 152734945705280, 111, '2025-09-23 11:28:58.163', '2025-09-23 11:28:58.163');
INSERT INTO public.t_ds_trigger_relation VALUES (215, 2, 152734969078080, 113, '2025-09-23 11:29:22.365', '2025-09-23 11:29:22.365');
INSERT INTO public.t_ds_trigger_relation VALUES (216, 0, 152734969078080, 112, '2025-09-23 11:29:22.929', '2025-09-23 11:29:22.929');
INSERT INTO public.t_ds_trigger_relation VALUES (217, 2, 152735061147968, 114, '2025-09-23 11:30:57.435', '2025-09-23 11:30:57.435');
INSERT INTO public.t_ds_trigger_relation VALUES (218, 0, 152735061147968, 113, '2025-09-23 11:30:58.28', '2025-09-23 11:30:58.28');
INSERT INTO public.t_ds_trigger_relation VALUES (219, 2, 152735063695680, 115, '2025-09-23 11:30:59.924', '2025-09-23 11:30:59.924');
INSERT INTO public.t_ds_trigger_relation VALUES (220, 0, 152735063695680, 114, '2025-09-23 11:31:00.294', '2025-09-23 11:31:00.294');
INSERT INTO public.t_ds_trigger_relation VALUES (221, 2, 152735065461056, 116, '2025-09-23 11:31:01.647', '2025-09-23 11:31:01.647');
INSERT INTO public.t_ds_trigger_relation VALUES (222, 0, 152735065461056, 115, '2025-09-23 11:31:02.307', '2025-09-23 11:31:02.307');
INSERT INTO public.t_ds_trigger_relation VALUES (223, 2, 152735067200832, 117, '2025-09-23 11:31:03.345', '2025-09-23 11:31:03.345');
INSERT INTO public.t_ds_trigger_relation VALUES (224, 0, 152735067200832, 116, '2025-09-23 11:31:04.321', '2025-09-23 11:31:04.321');
INSERT INTO public.t_ds_trigger_relation VALUES (225, 2, 152735068927296, 118, '2025-09-23 11:31:05.032', '2025-09-23 11:31:05.032');
INSERT INTO public.t_ds_trigger_relation VALUES (226, 0, 152735068927296, 117, '2025-09-23 11:31:05.333', '2025-09-23 11:31:05.333');
INSERT INTO public.t_ds_trigger_relation VALUES (227, 2, 152735070760256, 119, '2025-09-23 11:31:06.822', '2025-09-23 11:31:06.822');
INSERT INTO public.t_ds_trigger_relation VALUES (228, 0, 152735070760256, 118, '2025-09-23 11:31:07.345', '2025-09-23 11:31:07.345');
INSERT INTO public.t_ds_trigger_relation VALUES (229, 2, 152735071800640, 120, '2025-09-23 11:31:07.838', '2025-09-23 11:31:07.838');
INSERT INTO public.t_ds_trigger_relation VALUES (230, 0, 152735071800640, 119, '2025-09-23 11:31:08.355', '2025-09-23 11:31:08.355');
INSERT INTO public.t_ds_trigger_relation VALUES (231, 2, 152735180767552, 121, '2025-09-23 11:33:01.131', '2025-09-23 11:33:01.131');
INSERT INTO public.t_ds_trigger_relation VALUES (232, 0, 152735180767552, 120, '2025-09-23 11:33:01.485', '2025-09-23 11:33:01.485');
INSERT INTO public.t_ds_trigger_relation VALUES (233, 2, 152735183215936, 122, '2025-09-23 11:33:03.52', '2025-09-23 11:33:03.52');
INSERT INTO public.t_ds_trigger_relation VALUES (234, 0, 152735183215936, 121, '2025-09-23 11:33:04.51', '2025-09-23 11:33:04.51');
INSERT INTO public.t_ds_trigger_relation VALUES (235, 2, 152735185019200, 123, '2025-09-23 11:33:05.282', '2025-09-23 11:33:05.282');
INSERT INTO public.t_ds_trigger_relation VALUES (236, 0, 152735185019200, 122, '2025-09-23 11:33:05.865', '2025-09-23 11:33:05.865');
INSERT INTO public.t_ds_trigger_relation VALUES (237, 2, 152735186897216, 124, '2025-09-23 11:33:07.115', '2025-09-23 11:33:07.115');
INSERT INTO public.t_ds_trigger_relation VALUES (238, 0, 152735186897216, 123, '2025-09-23 11:33:07.891', '2025-09-23 11:33:07.891');
INSERT INTO public.t_ds_trigger_relation VALUES (239, 2, 152735188919616, 125, '2025-09-23 11:33:09.091', '2025-09-23 11:33:09.091');
INSERT INTO public.t_ds_trigger_relation VALUES (240, 0, 152735188919616, 124, '2025-09-23 11:33:09.913', '2025-09-23 11:33:09.913');
INSERT INTO public.t_ds_trigger_relation VALUES (241, 2, 152735418829120, 126, '2025-09-23 11:37:06.356', '2025-09-23 11:37:06.356');
INSERT INTO public.t_ds_trigger_relation VALUES (242, 0, 152735418829120, 125, '2025-09-23 11:37:07.076', '2025-09-23 11:37:07.076');
INSERT INTO public.t_ds_trigger_relation VALUES (244, 0, 152735421028672, 126, '2025-09-23 11:37:09.1', '2025-09-23 11:37:09.1');
INSERT INTO public.t_ds_trigger_relation VALUES (246, 0, 152735422943552, 127, '2025-09-23 11:37:11.113', '2025-09-23 11:37:11.113');
INSERT INTO public.t_ds_trigger_relation VALUES (248, 0, 152735424813376, 128, '2025-09-23 11:37:13.128', '2025-09-23 11:37:13.128');
INSERT INTO public.t_ds_trigger_relation VALUES (250, 0, 152735426528576, 129, '2025-09-23 11:37:14.141', '2025-09-23 11:37:14.141');
INSERT INTO public.t_ds_trigger_relation VALUES (243, 2, 152735421028672, 127, '2025-09-23 11:37:08.504', '2025-09-23 11:37:08.504');
INSERT INTO public.t_ds_trigger_relation VALUES (245, 2, 152735422943552, 128, '2025-09-23 11:37:10.375', '2025-09-23 11:37:10.375');
INSERT INTO public.t_ds_trigger_relation VALUES (247, 2, 152735424813376, 129, '2025-09-23 11:37:12.199', '2025-09-23 11:37:12.199');
INSERT INTO public.t_ds_trigger_relation VALUES (249, 2, 152735426528576, 130, '2025-09-23 11:37:13.874', '2025-09-23 11:37:13.874');
INSERT INTO public.t_ds_trigger_relation VALUES (251, 2, 153378465695264, 138, '2025-09-30 14:34:23.892', '2025-09-30 14:34:23.892');
INSERT INTO public.t_ds_trigger_relation VALUES (252, 0, 153378465695264, 137, '2025-09-30 14:34:24.678', '2025-09-30 14:34:24.678');
INSERT INTO public.t_ds_trigger_relation VALUES (253, 2, 153378993323552, 139, '2025-09-30 14:43:47.341', '2025-09-30 14:43:47.341');
INSERT INTO public.t_ds_trigger_relation VALUES (254, 0, 153378993323552, 138, '2025-09-30 14:43:47.932', '2025-09-30 14:43:47.932');
INSERT INTO public.t_ds_trigger_relation VALUES (255, 2, 153379095439904, 140, '2025-09-30 14:45:35.293', '2025-09-30 14:45:35.293');
INSERT INTO public.t_ds_trigger_relation VALUES (256, 0, 153379095439904, 139, '2025-09-30 14:45:35.375', '2025-09-30 14:45:35.375');
INSERT INTO public.t_ds_trigger_relation VALUES (257, 2, 153379304974880, 141, '2025-09-30 14:49:17.987', '2025-09-30 14:49:17.987');
INSERT INTO public.t_ds_trigger_relation VALUES (258, 0, 153379304974880, 140, '2025-09-30 14:49:18.327', '2025-09-30 14:49:18.327');
INSERT INTO public.t_ds_trigger_relation VALUES (259, 2, 153379427285536, 142, '2025-09-30 14:51:26.746', '2025-09-30 14:51:26.746');
INSERT INTO public.t_ds_trigger_relation VALUES (260, 0, 153379427285536, 141, '2025-09-30 14:51:26.849', '2025-09-30 14:51:26.849');
INSERT INTO public.t_ds_trigger_relation VALUES (261, 2, 153379499111968, 143, '2025-09-30 14:52:38.928', '2025-09-30 14:52:38.928');
INSERT INTO public.t_ds_trigger_relation VALUES (262, 0, 153379499111968, 142, '2025-09-30 14:52:39.276', '2025-09-30 14:52:39.276');
INSERT INTO public.t_ds_trigger_relation VALUES (263, 2, 153379736836640, 144, '2025-09-30 14:56:45.173', '2025-09-30 14:56:45.173');
INSERT INTO public.t_ds_trigger_relation VALUES (264, 0, 153379736836640, 143, '2025-09-30 14:56:45.897', '2025-09-30 14:56:45.897');
INSERT INTO public.t_ds_trigger_relation VALUES (265, 2, 153379749288480, 145, '2025-09-30 14:56:57.33', '2025-09-30 14:56:57.33');
INSERT INTO public.t_ds_trigger_relation VALUES (266, 0, 153379749288480, 144, '2025-09-30 14:56:57.943', '2025-09-30 14:56:57.943');
INSERT INTO public.t_ds_trigger_relation VALUES (267, 2, 153381090353696, 146, '2025-09-30 15:20:11.488', '2025-09-30 15:20:11.488');
INSERT INTO public.t_ds_trigger_relation VALUES (268, 0, 153381090353696, 145, '2025-09-30 15:20:12.256', '2025-09-30 15:20:12.256');
INSERT INTO public.t_ds_trigger_relation VALUES (269, 2, 153381462225440, 147, '2025-09-30 15:26:38.559', '2025-09-30 15:26:38.559');
INSERT INTO public.t_ds_trigger_relation VALUES (270, 0, 153381462225440, 146, '2025-09-30 15:26:39.059', '2025-09-30 15:26:39.059');
INSERT INTO public.t_ds_trigger_relation VALUES (271, 2, 153381691244064, 148, '2025-09-30 15:30:37.299', '2025-09-30 15:30:37.299');
INSERT INTO public.t_ds_trigger_relation VALUES (272, 0, 153381691244064, 147, '2025-09-30 15:30:37.758', '2025-09-30 15:30:37.758');
INSERT INTO public.t_ds_trigger_relation VALUES (273, 2, 153381693837856, 149, '2025-09-30 15:30:39.831', '2025-09-30 15:30:39.831');
INSERT INTO public.t_ds_trigger_relation VALUES (274, 0, 153381693837856, 148, '2025-09-30 15:30:40.789', '2025-09-30 15:30:40.789');
INSERT INTO public.t_ds_trigger_relation VALUES (275, 2, 153381792671264, 150, '2025-09-30 15:32:24.068', '2025-09-30 15:32:24.068');
INSERT INTO public.t_ds_trigger_relation VALUES (276, 0, 153381792671264, 149, '2025-09-30 15:32:24.743', '2025-09-30 15:32:24.743');
INSERT INTO public.t_ds_trigger_relation VALUES (277, 2, 153381940552224, 151, '2025-09-30 15:35:00.454', '2025-09-30 15:35:00.454');
INSERT INTO public.t_ds_trigger_relation VALUES (278, 0, 153381940552224, 150, '2025-09-30 15:35:00.945', '2025-09-30 15:35:00.945');
INSERT INTO public.t_ds_trigger_relation VALUES (279, 2, 153382001882656, 152, '2025-09-30 15:36:05.183', '2025-09-30 15:36:05.183');
INSERT INTO public.t_ds_trigger_relation VALUES (280, 0, 153382001882656, 151, '2025-09-30 15:36:05.916', '2025-09-30 15:36:05.916');
INSERT INTO public.t_ds_trigger_relation VALUES (281, 2, 153382030367264, 153, '2025-09-30 15:36:33.007', '2025-09-30 15:36:33.007');
INSERT INTO public.t_ds_trigger_relation VALUES (282, 0, 153382030367264, 152, '2025-09-30 15:36:33.972', '2025-09-30 15:36:33.972');
INSERT INTO public.t_ds_trigger_relation VALUES (283, 2, 153382200682016, 154, '2025-09-30 15:39:29.007', '2025-09-30 15:39:29.007');
INSERT INTO public.t_ds_trigger_relation VALUES (284, 0, 153382200682016, 153, '2025-09-30 15:39:29.914', '2025-09-30 15:39:29.914');
INSERT INTO public.t_ds_trigger_relation VALUES (285, 2, 153382310732320, 155, '2025-09-30 15:41:22.046', '2025-09-30 15:41:22.046');
INSERT INTO public.t_ds_trigger_relation VALUES (286, 0, 153382310732320, 154, '2025-09-30 15:41:22.685', '2025-09-30 15:41:22.685');
INSERT INTO public.t_ds_trigger_relation VALUES (287, 2, 153382472933920, 156, '2025-09-30 15:44:11.399', '2025-09-30 15:44:11.399');
INSERT INTO public.t_ds_trigger_relation VALUES (288, 0, 153382472933920, 155, '2025-09-30 15:44:11.969', '2025-09-30 15:44:11.969');
INSERT INTO public.t_ds_trigger_relation VALUES (289, 2, 153382555132448, 157, '2025-09-30 15:45:36.681', '2025-09-30 15:45:36.681');
INSERT INTO public.t_ds_trigger_relation VALUES (290, 0, 153382555132448, 156, '2025-09-30 15:45:37.12', '2025-09-30 15:45:37.12');
INSERT INTO public.t_ds_trigger_relation VALUES (291, 2, 153382655880736, 158, '2025-09-30 15:47:22.463', '2025-09-30 15:47:22.463');
INSERT INTO public.t_ds_trigger_relation VALUES (292, 0, 153382655880736, 157, '2025-09-30 15:47:22.683', '2025-09-30 15:47:22.683');
INSERT INTO public.t_ds_trigger_relation VALUES (293, 2, 153382731325984, 159, '2025-09-30 15:48:40.666', '2025-09-30 15:48:40.666');
INSERT INTO public.t_ds_trigger_relation VALUES (294, 0, 153382731325984, 158, '2025-09-30 15:48:41.335', '2025-09-30 15:48:41.335');
INSERT INTO public.t_ds_trigger_relation VALUES (295, 2, 153382856590880, 160, '2025-09-30 15:50:52.7', '2025-09-30 15:50:52.7');
INSERT INTO public.t_ds_trigger_relation VALUES (296, 0, 153382856590880, 159, '2025-09-30 15:50:53.24', '2025-09-30 15:50:53.24');
INSERT INTO public.t_ds_trigger_relation VALUES (297, 2, 153383125325344, 161, '2025-09-30 15:55:31.847', '2025-09-30 15:55:31.847');
INSERT INTO public.t_ds_trigger_relation VALUES (298, 0, 153383125325344, 160, '2025-09-30 15:55:32.455', '2025-09-30 15:55:32.455');
INSERT INTO public.t_ds_trigger_relation VALUES (299, 2, 153383253207584, 162, '2025-09-30 15:57:44.091', '2025-09-30 15:57:44.091');
INSERT INTO public.t_ds_trigger_relation VALUES (300, 0, 153383253207584, 161, '2025-09-30 15:57:45.077', '2025-09-30 15:57:45.077');
INSERT INTO public.t_ds_trigger_relation VALUES (301, 2, 153383407263264, 163, '2025-09-30 16:00:21.998', '2025-09-30 16:00:21.998');
INSERT INTO public.t_ds_trigger_relation VALUES (302, 0, 153383407263264, 162, '2025-09-30 16:00:22.842', '2025-09-30 16:00:22.842');
INSERT INTO public.t_ds_trigger_relation VALUES (303, 2, 153383586356768, 164, '2025-09-30 16:03:32.474', '2025-09-30 16:03:32.474');
INSERT INTO public.t_ds_trigger_relation VALUES (304, 0, 153383586356768, 163, '2025-09-30 16:03:32.724', '2025-09-30 16:03:32.724');
INSERT INTO public.t_ds_trigger_relation VALUES (305, 2, 153383710234144, 165, '2025-09-30 16:05:44.512', '2025-09-30 16:05:44.512');
INSERT INTO public.t_ds_trigger_relation VALUES (306, 0, 153383710234144, 164, '2025-09-30 16:05:45.034', '2025-09-30 16:05:45.034');
INSERT INTO public.t_ds_trigger_relation VALUES (307, 2, 153383889923616, 166, '2025-09-30 16:08:53.72', '2025-09-30 16:08:53.72');
INSERT INTO public.t_ds_trigger_relation VALUES (308, 0, 153383889923616, 165, '2025-09-30 16:08:54.085', '2025-09-30 16:08:54.085');


--
-- Data for Name: t_ds_udfs; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Data for Name: t_ds_user; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_user VALUES (1, 'admin', '7ad2410b2f4c074479a8937a28a22b8f', 0, 'xxx@qq.com', '', -1, '2018-03-27 15:48:50', '2018-10-24 17:40:22', NULL, 1, NULL);


--
-- Data for Name: t_ds_version; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.t_ds_version VALUES (1, '3.3.0');


--
-- Data for Name: t_ds_worker_group; Type: TABLE DATA; Schema: public; Owner: root
--



--
-- Name: t_ds_access_token_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_access_token_id_sequence', 33, true);


--
-- Name: t_ds_alert_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_alert_id_sequence', 543, true);


--
-- Name: t_ds_alert_plugin_instance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_alert_plugin_instance_id_seq', 1, false);


--
-- Name: t_ds_alert_send_status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_alert_send_status_id_seq', 1, false);


--
-- Name: t_ds_alertgroup_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_alertgroup_id_sequence', 33, true);


--
-- Name: t_ds_audit_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_audit_log_id_seq', 792, true);


--
-- Name: t_ds_cluster_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_cluster_id_seq', 1, false);


--
-- Name: t_ds_command_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_command_id_sequence', 166, true);


--
-- Name: t_ds_datasource_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_datasource_id_sequence', 1, false);


--
-- Name: t_ds_dq_comparison_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_comparison_type_id_seq', 1, false);


--
-- Name: t_ds_dq_execute_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_execute_result_id_seq', 1, false);


--
-- Name: t_ds_dq_rule_execute_sql_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_rule_execute_sql_id_seq', 1, false);


--
-- Name: t_ds_dq_rule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_rule_id_seq', 1, false);


--
-- Name: t_ds_dq_rule_input_entry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_rule_input_entry_id_seq', 1, false);


--
-- Name: t_ds_dq_task_statistics_value_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_dq_task_statistics_value_id_seq', 1, false);


--
-- Name: t_ds_environment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_environment_id_seq', 33, true);


--
-- Name: t_ds_environment_worker_group_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_environment_worker_group_relation_id_seq', 1, true);


--
-- Name: t_ds_fav_task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_fav_task_id_seq', 1, false);


--
-- Name: t_ds_k8s_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_k8s_id_seq', 1, false);


--
-- Name: t_ds_k8s_namespace_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_k8s_namespace_id_seq', 1, false);


--
-- Name: t_ds_plugin_define_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_plugin_define_id_seq', 66, true);


--
-- Name: t_ds_process_definition_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_process_definition_id_sequence', 1, false);


--
-- Name: t_ds_process_definition_log_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_process_definition_log_id_sequence', 194, true);


--
-- Name: t_ds_process_instance_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_process_instance_id_sequence', 165, true);


--
-- Name: t_ds_process_task_relation_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_process_task_relation_id_sequence', 223, true);


--
-- Name: t_ds_process_task_relation_log_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_process_task_relation_log_id_sequence', 223, true);


--
-- Name: t_ds_project_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_project_id_sequence', 36, true);


--
-- Name: t_ds_project_parameter_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_project_parameter_id_sequence', 1, false);


--
-- Name: t_ds_project_preference_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_project_preference_id_sequence', 1, false);


--
-- Name: t_ds_queue_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_queue_id_sequence', 33, true);


--
-- Name: t_ds_relation_datasource_user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_datasource_user_id_sequence', 1, false);


--
-- Name: t_ds_relation_namespace_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_namespace_user_id_seq', 1, false);


--
-- Name: t_ds_relation_process_instance_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_process_instance_id_sequence', 3, true);


--
-- Name: t_ds_relation_project_user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_project_user_id_sequence', 1, false);


--
-- Name: t_ds_relation_project_worker_group_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_project_worker_group_sequence', 1, false);


--
-- Name: t_ds_relation_resources_user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_resources_user_id_sequence', 1, false);


--
-- Name: t_ds_relation_rule_execute_sql_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_rule_execute_sql_id_seq', 1, false);


--
-- Name: t_ds_relation_rule_input_entry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_rule_input_entry_id_seq', 1, false);


--
-- Name: t_ds_relation_sub_workflow_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_sub_workflow_id_seq', 1, false);


--
-- Name: t_ds_relation_udfs_user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_relation_udfs_user_id_sequence', 1, false);


--
-- Name: t_ds_resources_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_resources_id_sequence', 1, false);


--
-- Name: t_ds_schedules_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_schedules_id_sequence', 18, true);


--
-- Name: t_ds_task_definition_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_task_definition_id_sequence', 108, true);


--
-- Name: t_ds_task_definition_log_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_task_definition_log_id_sequence', 218, true);


--
-- Name: t_ds_task_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_task_group_id_seq', 1, false);


--
-- Name: t_ds_task_group_queue_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_task_group_queue_id_seq', 1, false);


--
-- Name: t_ds_task_instance_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_task_instance_id_sequence', 168, true);


--
-- Name: t_ds_tenant_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_tenant_id_sequence', 1, false);


--
-- Name: t_ds_trigger_relation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_trigger_relation_id_seq', 308, true);


--
-- Name: t_ds_udfs_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_udfs_id_sequence', 1, false);


--
-- Name: t_ds_user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_user_id_sequence', 33, true);


--
-- Name: t_ds_version_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_version_id_sequence', 33, true);


--
-- Name: t_ds_worker_group_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.t_ds_worker_group_id_sequence', 1, false);


--
-- Name: t_ds_alert_send_status alert_send_status_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert_send_status
    ADD CONSTRAINT alert_send_status_unique UNIQUE (alert_id, alert_plugin_instance_id);


--
-- Name: t_ds_cluster cluster_code_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_cluster
    ADD CONSTRAINT cluster_code_unique UNIQUE (code);


--
-- Name: t_ds_cluster cluster_name_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_cluster
    ADD CONSTRAINT cluster_name_unique UNIQUE (name);


--
-- Name: t_ds_environment environment_code_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment
    ADD CONSTRAINT environment_code_unique UNIQUE (code);


--
-- Name: t_ds_environment environment_name_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment
    ADD CONSTRAINT environment_name_unique UNIQUE (name);


--
-- Name: t_ds_environment_worker_group_relation environment_worker_group_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment_worker_group_relation
    ADD CONSTRAINT environment_worker_group_unique UNIQUE (environment_code, worker_group);


--
-- Name: t_ds_k8s_namespace k8s_namespace_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_k8s_namespace
    ADD CONSTRAINT k8s_namespace_unique UNIQUE (namespace, cluster_code);


--
-- Name: t_ds_worker_group name_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_worker_group
    ADD CONSTRAINT name_unique UNIQUE (name);


--
-- Name: t_ds_relation_namespace_user namespace_user_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_namespace_user
    ADD CONSTRAINT namespace_user_unique UNIQUE (user_id, namespace_id);


--
-- Name: t_ds_process_definition process_definition_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_definition
    ADD CONSTRAINT process_definition_unique UNIQUE (name, project_code);


--
-- Name: qrtz_blob_triggers qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: qrtz_calendars qrtz_calendars_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);


--
-- Name: qrtz_cron_triggers qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: qrtz_fired_triggers qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);


--
-- Name: qrtz_job_details qrtz_job_details_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);


--
-- Name: qrtz_locks qrtz_locks_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);


--
-- Name: qrtz_paused_trigger_grps qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);


--
-- Name: qrtz_scheduler_state qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);


--
-- Name: qrtz_simple_triggers qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: qrtz_simprop_triggers qrtz_simprop_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: qrtz_triggers qrtz_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: t_ds_access_token t_ds_access_token_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_access_token
    ADD CONSTRAINT t_ds_access_token_pkey PRIMARY KEY (id);


--
-- Name: t_ds_alert t_ds_alert_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert
    ADD CONSTRAINT t_ds_alert_pkey PRIMARY KEY (id);


--
-- Name: t_ds_alert_plugin_instance t_ds_alert_plugin_instance_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert_plugin_instance
    ADD CONSTRAINT t_ds_alert_plugin_instance_pk PRIMARY KEY (id);


--
-- Name: t_ds_alert_send_status t_ds_alert_send_status_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alert_send_status
    ADD CONSTRAINT t_ds_alert_send_status_pkey PRIMARY KEY (id);


--
-- Name: t_ds_alertgroup t_ds_alertgroup_name_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alertgroup
    ADD CONSTRAINT t_ds_alertgroup_name_un UNIQUE (group_name);


--
-- Name: t_ds_alertgroup t_ds_alertgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_alertgroup
    ADD CONSTRAINT t_ds_alertgroup_pkey PRIMARY KEY (id);


--
-- Name: t_ds_audit_log t_ds_audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_audit_log
    ADD CONSTRAINT t_ds_audit_log_pkey PRIMARY KEY (id);


--
-- Name: t_ds_cluster t_ds_cluster_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_cluster
    ADD CONSTRAINT t_ds_cluster_pkey PRIMARY KEY (id);


--
-- Name: t_ds_command t_ds_command_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_command
    ADD CONSTRAINT t_ds_command_pkey PRIMARY KEY (id);


--
-- Name: t_ds_datasource t_ds_datasource_name_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_datasource
    ADD CONSTRAINT t_ds_datasource_name_un UNIQUE (name, type);


--
-- Name: t_ds_datasource t_ds_datasource_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_datasource
    ADD CONSTRAINT t_ds_datasource_pkey PRIMARY KEY (id);


--
-- Name: t_ds_dq_comparison_type t_ds_dq_comparison_type_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_comparison_type
    ADD CONSTRAINT t_ds_dq_comparison_type_pk PRIMARY KEY (id);


--
-- Name: t_ds_dq_execute_result t_ds_dq_execute_result_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_execute_result
    ADD CONSTRAINT t_ds_dq_execute_result_pk PRIMARY KEY (id);


--
-- Name: t_ds_dq_rule_execute_sql t_ds_dq_rule_execute_sql_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule_execute_sql
    ADD CONSTRAINT t_ds_dq_rule_execute_sql_pk PRIMARY KEY (id);


--
-- Name: t_ds_dq_rule_input_entry t_ds_dq_rule_input_entry_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule_input_entry
    ADD CONSTRAINT t_ds_dq_rule_input_entry_pk PRIMARY KEY (id);


--
-- Name: t_ds_dq_rule t_ds_dq_rule_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_rule
    ADD CONSTRAINT t_ds_dq_rule_pk PRIMARY KEY (id);


--
-- Name: t_ds_dq_task_statistics_value t_ds_dq_task_statistics_value_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_dq_task_statistics_value
    ADD CONSTRAINT t_ds_dq_task_statistics_value_pk PRIMARY KEY (id);


--
-- Name: t_ds_environment t_ds_environment_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment
    ADD CONSTRAINT t_ds_environment_pkey PRIMARY KEY (id);


--
-- Name: t_ds_environment_worker_group_relation t_ds_environment_worker_group_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_environment_worker_group_relation
    ADD CONSTRAINT t_ds_environment_worker_group_relation_pkey PRIMARY KEY (id);


--
-- Name: t_ds_error_command t_ds_error_command_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_error_command
    ADD CONSTRAINT t_ds_error_command_pkey PRIMARY KEY (id);


--
-- Name: t_ds_fav_task t_ds_fav_task_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_fav_task
    ADD CONSTRAINT t_ds_fav_task_pkey PRIMARY KEY (id);


--
-- Name: t_ds_k8s_namespace t_ds_k8s_namespace_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_k8s_namespace
    ADD CONSTRAINT t_ds_k8s_namespace_pkey PRIMARY KEY (id);


--
-- Name: t_ds_k8s t_ds_k8s_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_k8s
    ADD CONSTRAINT t_ds_k8s_pkey PRIMARY KEY (id);


--
-- Name: t_ds_listener_event t_ds_listener_event_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_listener_event
    ADD CONSTRAINT t_ds_listener_event_pkey PRIMARY KEY (id);


--
-- Name: t_ds_plugin_define t_ds_plugin_define_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_plugin_define
    ADD CONSTRAINT t_ds_plugin_define_pk PRIMARY KEY (id);


--
-- Name: t_ds_plugin_define t_ds_plugin_define_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_plugin_define
    ADD CONSTRAINT t_ds_plugin_define_un UNIQUE (plugin_name, plugin_type);


--
-- Name: t_ds_process_definition_log t_ds_process_definition_log_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_definition_log
    ADD CONSTRAINT t_ds_process_definition_log_pkey PRIMARY KEY (id);


--
-- Name: t_ds_process_definition t_ds_process_definition_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_definition
    ADD CONSTRAINT t_ds_process_definition_pkey PRIMARY KEY (id);


--
-- Name: t_ds_process_instance t_ds_process_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_instance
    ADD CONSTRAINT t_ds_process_instance_pkey PRIMARY KEY (id);


--
-- Name: t_ds_process_task_relation_log t_ds_process_task_relation_log_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_task_relation_log
    ADD CONSTRAINT t_ds_process_task_relation_log_pkey PRIMARY KEY (id);


--
-- Name: t_ds_process_task_relation t_ds_process_task_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_process_task_relation
    ADD CONSTRAINT t_ds_process_task_relation_pkey PRIMARY KEY (id);


--
-- Name: t_ds_project_parameter t_ds_project_parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_project_parameter
    ADD CONSTRAINT t_ds_project_parameter_pkey PRIMARY KEY (id);


--
-- Name: t_ds_project t_ds_project_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_project
    ADD CONSTRAINT t_ds_project_pkey PRIMARY KEY (id);


--
-- Name: t_ds_project_preference t_ds_project_preference_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_project_preference
    ADD CONSTRAINT t_ds_project_preference_pkey PRIMARY KEY (id);


--
-- Name: t_ds_queue t_ds_queue_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_queue
    ADD CONSTRAINT t_ds_queue_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_datasource_user t_ds_relation_datasource_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_datasource_user
    ADD CONSTRAINT t_ds_relation_datasource_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_namespace_user t_ds_relation_namespace_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_namespace_user
    ADD CONSTRAINT t_ds_relation_namespace_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_process_instance t_ds_relation_process_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_process_instance
    ADD CONSTRAINT t_ds_relation_process_instance_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_project_user t_ds_relation_project_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_project_user
    ADD CONSTRAINT t_ds_relation_project_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_project_user t_ds_relation_project_user_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_project_user
    ADD CONSTRAINT t_ds_relation_project_user_un UNIQUE (user_id, project_id);


--
-- Name: t_ds_relation_project_worker_group t_ds_relation_project_worker_group_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_project_worker_group
    ADD CONSTRAINT t_ds_relation_project_worker_group_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_project_worker_group t_ds_relation_project_worker_group_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_project_worker_group
    ADD CONSTRAINT t_ds_relation_project_worker_group_un UNIQUE (project_code, worker_group);


--
-- Name: t_ds_relation_resources_user t_ds_relation_resources_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_resources_user
    ADD CONSTRAINT t_ds_relation_resources_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_rule_execute_sql t_ds_relation_rule_execute_sql_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_rule_execute_sql
    ADD CONSTRAINT t_ds_relation_rule_execute_sql_pk PRIMARY KEY (id);


--
-- Name: t_ds_relation_rule_input_entry t_ds_relation_rule_input_entry_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_rule_input_entry
    ADD CONSTRAINT t_ds_relation_rule_input_entry_pk PRIMARY KEY (id);


--
-- Name: t_ds_relation_sub_workflow t_ds_relation_sub_workflow_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_sub_workflow
    ADD CONSTRAINT t_ds_relation_sub_workflow_pkey PRIMARY KEY (id);


--
-- Name: t_ds_relation_udfs_user t_ds_relation_udfs_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_relation_udfs_user
    ADD CONSTRAINT t_ds_relation_udfs_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_resources t_ds_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_resources
    ADD CONSTRAINT t_ds_resources_pkey PRIMARY KEY (id);


--
-- Name: t_ds_resources t_ds_resources_un; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_resources
    ADD CONSTRAINT t_ds_resources_un UNIQUE (full_name, type);


--
-- Name: t_ds_schedules t_ds_schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_schedules
    ADD CONSTRAINT t_ds_schedules_pkey PRIMARY KEY (id);


--
-- Name: t_ds_session t_ds_session_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_session
    ADD CONSTRAINT t_ds_session_pkey PRIMARY KEY (id);


--
-- Name: t_ds_task_definition_log t_ds_task_definition_log_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_definition_log
    ADD CONSTRAINT t_ds_task_definition_log_pkey PRIMARY KEY (id);


--
-- Name: t_ds_task_definition t_ds_task_definition_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_definition
    ADD CONSTRAINT t_ds_task_definition_pkey PRIMARY KEY (id);


--
-- Name: t_ds_task_group t_ds_task_group_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_group
    ADD CONSTRAINT t_ds_task_group_pkey PRIMARY KEY (id);


--
-- Name: t_ds_task_group_queue t_ds_task_group_queue_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_group_queue
    ADD CONSTRAINT t_ds_task_group_queue_pkey PRIMARY KEY (id);


--
-- Name: t_ds_task_instance t_ds_task_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_task_instance
    ADD CONSTRAINT t_ds_task_instance_pkey PRIMARY KEY (id);


--
-- Name: t_ds_tenant t_ds_tenant_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_tenant
    ADD CONSTRAINT t_ds_tenant_pkey PRIMARY KEY (id);


--
-- Name: t_ds_trigger_relation t_ds_trigger_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_trigger_relation
    ADD CONSTRAINT t_ds_trigger_relation_pkey PRIMARY KEY (id);


--
-- Name: t_ds_trigger_relation t_ds_trigger_relation_unique; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_trigger_relation
    ADD CONSTRAINT t_ds_trigger_relation_unique UNIQUE (trigger_type, job_id, trigger_code);


--
-- Name: t_ds_udfs t_ds_udfs_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_udfs
    ADD CONSTRAINT t_ds_udfs_pkey PRIMARY KEY (id);


--
-- Name: t_ds_user t_ds_user_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_user
    ADD CONSTRAINT t_ds_user_pkey PRIMARY KEY (id);


--
-- Name: t_ds_version t_ds_version_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_version
    ADD CONSTRAINT t_ds_version_pkey PRIMARY KEY (id);


--
-- Name: t_ds_worker_group t_ds_worker_group_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.t_ds_worker_group
    ADD CONSTRAINT t_ds_worker_group_pkey PRIMARY KEY (id);


--
-- Name: idx_cache_key; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_cache_key ON public.t_ds_task_instance USING btree (cache_key);


--
-- Name: idx_listener_event_post_status; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_listener_event_post_status ON public.t_ds_listener_event USING btree (post_status);


--
-- Name: idx_listener_event_sign; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_listener_event_sign ON public.t_ds_listener_event USING btree (sign);


--
-- Name: idx_parent_task_code; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_parent_task_code ON public.t_ds_relation_sub_workflow USING btree (parent_task_code);


--
-- Name: idx_parent_workflow_instance_id; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_parent_workflow_instance_id ON public.t_ds_relation_sub_workflow USING btree (parent_workflow_instance_id);


--
-- Name: idx_qrtz_ft_inst_job_req_rcvry; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON public.qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);


--
-- Name: idx_qrtz_ft_j_g; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_j_g ON public.qrtz_fired_triggers USING btree (sched_name, job_name, job_group);


--
-- Name: idx_qrtz_ft_jg; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_jg ON public.qrtz_fired_triggers USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_ft_t_g; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_t_g ON public.qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);


--
-- Name: idx_qrtz_ft_tg; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_tg ON public.qrtz_fired_triggers USING btree (sched_name, trigger_group);


--
-- Name: idx_qrtz_ft_trig_inst_name; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_ft_trig_inst_name ON public.qrtz_fired_triggers USING btree (sched_name, instance_name);


--
-- Name: idx_qrtz_j_grp; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_j_grp ON public.qrtz_job_details USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_j_req_recovery; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_j_req_recovery ON public.qrtz_job_details USING btree (sched_name, requests_recovery);


--
-- Name: idx_qrtz_t_c; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_c ON public.qrtz_triggers USING btree (sched_name, calendar_name);


--
-- Name: idx_qrtz_t_g; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_g ON public.qrtz_triggers USING btree (sched_name, trigger_group);


--
-- Name: idx_qrtz_t_j; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_j ON public.qrtz_triggers USING btree (sched_name, job_name, job_group);


--
-- Name: idx_qrtz_t_jg; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_jg ON public.qrtz_triggers USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_t_n_g_state; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_n_g_state ON public.qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_n_state; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_n_state ON public.qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_next_fire_time; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_next_fire_time ON public.qrtz_triggers USING btree (sched_name, next_fire_time);


--
-- Name: idx_qrtz_t_nft_misfire; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_nft_misfire ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);


--
-- Name: idx_qrtz_t_nft_st; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_nft_st ON public.qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);


--
-- Name: idx_qrtz_t_nft_st_misfire; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_nft_st_misfire ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);


--
-- Name: idx_qrtz_t_nft_st_misfire_grp; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON public.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_state; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_qrtz_t_state ON public.qrtz_triggers USING btree (sched_name, trigger_state);


--
-- Name: idx_relation_process_instance_parent_process_task; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_relation_process_instance_parent_process_task ON public.t_ds_relation_process_instance USING btree (parent_process_instance_id, parent_task_instance_id);


--
-- Name: idx_relation_process_instance_process_instance_id; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_relation_process_instance_process_instance_id ON public.t_ds_relation_process_instance USING btree (process_instance_id);


--
-- Name: idx_sign; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_sign ON public.t_ds_alert USING btree (sign);


--
-- Name: idx_status; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_status ON public.t_ds_alert USING btree (alert_status);


--
-- Name: idx_sub_workflow_instance_id; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_sub_workflow_instance_id ON public.t_ds_relation_sub_workflow USING btree (sub_workflow_instance_id);


--
-- Name: idx_t_ds_task_group_queue_in_queue; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_t_ds_task_group_queue_in_queue ON public.t_ds_task_group_queue USING btree (in_queue);


--
-- Name: idx_task_definition_log_code_version; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_task_definition_log_code_version ON public.t_ds_task_definition_log USING btree (code, version);


--
-- Name: idx_task_definition_log_project_code; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_task_definition_log_project_code ON public.t_ds_task_definition_log USING btree (project_code);


--
-- Name: idx_task_instance_code_version; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX idx_task_instance_code_version ON public.t_ds_task_instance USING btree (task_code, task_definition_version);


--
-- Name: priority_id_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX priority_id_index ON public.t_ds_command USING btree (process_instance_priority, id);


--
-- Name: process_definition_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_definition_index ON public.t_ds_process_definition USING btree (code, id);


--
-- Name: process_instance_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_instance_index ON public.t_ds_process_instance USING btree (process_definition_code, id);


--
-- Name: process_task_relation_idx_post_task_code_version; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_task_relation_idx_post_task_code_version ON public.t_ds_process_task_relation USING btree (post_task_code, post_task_version);


--
-- Name: process_task_relation_idx_pre_task_code_version; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_task_relation_idx_pre_task_code_version ON public.t_ds_process_task_relation USING btree (pre_task_code, pre_task_version);


--
-- Name: process_task_relation_idx_project_code_process_definition_code; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_task_relation_idx_project_code_process_definition_code ON public.t_ds_process_task_relation USING btree (project_code, process_definition_code);


--
-- Name: process_task_relation_log_idx_project_code_process_definition_c; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX process_task_relation_log_idx_project_code_process_definition_c ON public.t_ds_process_task_relation_log USING btree (project_code, process_definition_code);


--
-- Name: relation_project_user_id_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX relation_project_user_id_index ON public.t_ds_relation_project_user USING btree (user_id);


--
-- Name: start_time_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX start_time_index ON public.t_ds_process_instance USING btree (start_time, end_time);


--
-- Name: task_definition_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX task_definition_index ON public.t_ds_task_definition USING btree (project_code, id);


--
-- Name: uniq_idx_code_version; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX uniq_idx_code_version ON public.t_ds_process_definition_log USING btree (code, version);


--
-- Name: unique_code; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_code ON public.t_ds_project USING btree (code);


--
-- Name: unique_func_name; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_func_name ON public.t_ds_udfs USING btree (func_name);


--
-- Name: unique_name; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_name ON public.t_ds_project USING btree (name);


--
-- Name: unique_project_parameter_code; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_project_parameter_code ON public.t_ds_project_parameter USING btree (code);


--
-- Name: unique_project_parameter_name; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_project_parameter_name ON public.t_ds_project_parameter USING btree (project_code, param_name);


--
-- Name: unique_project_preference_code; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_project_preference_code ON public.t_ds_project_preference USING btree (code);


--
-- Name: unique_project_preference_project_code; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_project_preference_project_code ON public.t_ds_project_preference USING btree (project_code);


--
-- Name: unique_queue_name; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_queue_name ON public.t_ds_queue USING btree (queue_name);


--
-- Name: unique_tenant_code; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX unique_tenant_code ON public.t_ds_tenant USING btree (tenant_code);


--
-- Name: user_id_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX user_id_index ON public.t_ds_project USING btree (user_id);


--
-- Name: version_index; Type: INDEX; Schema: public; Owner: root
--

CREATE INDEX version_index ON public.t_ds_version USING btree (version);


--
-- PostgreSQL database dump complete
--

