DROP TABLE @.annotation CASCADE
##
DROP TABLE @.annotation_definition CASCADE
##
DROP TABLE @.annotation_definition_type CASCADE
##
DROP TABLE @.document_header_information CASCADE
##
DROP TABLE @.document_annotation_def_map CASCADE
##
DROP TABLE @.document_namespace_map CASCADE
##
DROP TABLE @.function_type CASCADE
##
DROP TABLE @.kam_edge CASCADE
##
DROP TABLE @.kam_edge_statement_map CASCADE
##
DROP TABLE @.kam_node CASCADE
##
DROP TABLE @.kam_node_parameter CASCADE
##
DROP TABLE @.kam_parameter_uuid CASCADE
##
DROP TABLE @.namespace CASCADE
##
DROP TABLE @.objects CASCADE
##
DROP TABLE @.objects_text CASCADE
##
DROP TABLE @.objects_type CASCADE
##
DROP TABLE @.relationship_type CASCADE
##
DROP TABLE @.statement CASCADE
##
DROP TABLE @.statement_annotation_map CASCADE
##
DROP TABLE @.term CASCADE
##
DROP TABLE @.term_parameter CASCADE
##
CREATE TABLE @.annotation (
    annotation_id                 integer NOT NULL,
    value_oid                     integer NOT NULL,
    annotation_definition_id      integer NOT NULL,
    PRIMARY KEY(annotation_id)
)
##
CREATE TABLE @.annotation_definition (
	annotation_definition_id     	integer NOT NULL,
	name                         	varchar(255) NOT NULL,
	description                  	varchar(255),
	annotation_usage               	varchar(255),
	domain_value_oid             	integer NOT NULL,
	annotation_definition_type_id	integer NOT NULL,
	CONSTRAINT annotation_definition_id_pk PRIMARY KEY(annotation_definition_id)
)
##
CREATE TABLE @.annotation_definition_type (
    annotation_definition_type_id    integer NOT NULL,
    name                             varchar(64) NOT NULL,
    PRIMARY KEY(annotation_definition_type_id)
)
##
CREATE TABLE @.document_header_information (
    document_id         integer NOT NULL,
    name                varchar(255) NOT NULL,
    description         varchar(255) NULL,
    version             varchar(64) NULL,
    copyright           varchar(4000) NULL,
    disclaimer          varchar(4000) NULL,
    contact_information varchar(4000) NULL,
    license_information varchar(4000) NULL,
    authors             varchar(255) NULL,
    PRIMARY KEY(document_id)
)
##
CREATE TABLE @.document_annotation_def_map (
    document_annotation_definition    serial,
    document_id                       integer NOT NULL,
    annotation_definition_id          integer NOT NULL,
    PRIMARY KEY(document_annotation_definition)
)
##
CREATE TABLE @.document_namespace_map (
    document_namespace_id    serial,
    document_id              integer NOT NULL,
    namespace_id             integer NOT NULL,
    PRIMARY KEY(document_namespace_id)
)
##
CREATE TABLE @.function_type (
    function_type_id    integer NOT NULL,
    name                varchar(64) NOT NULL,
    PRIMARY KEY(function_type_id)
)
##
CREATE TABLE @.kam_edge (
    kam_edge_id             integer NOT NULL,
    kam_source_node_id      integer NOT NULL,
    kam_target_node_id      integer NOT NULL,
    relationship_type_id    integer NOT NULL,
    PRIMARY KEY(kam_edge_id)
)
##
CREATE TABLE @.kam_node (
    kam_node_id         integer NOT NULL,
    function_type_id    integer NOT NULL,
    node_label_oid      integer NOT NULL,
    PRIMARY KEY(kam_node_id)
)
##
CREATE TABLE @.kam_node_parameter (
    kam_node_parameter_id    serial,
    kam_global_parameter_id  integer NOT NULL,
    kam_node_id              integer NOT NULL,
    ordinal                  integer NOT NULL,
    PRIMARY KEY(kam_node_parameter_id)
)
##
CREATE TABLE @.kam_parameter_uuid  ( 
    kam_parameter_uuid_id    serial,
    kam_global_parameter_id  integer NOT NULL,
    most_significant_bits    bigint NOT NULL,
    least_significant_bits   bigint NOT NULL,
    PRIMARY KEY(kam_parameter_uuid_id)
)
##
CREATE TABLE @.namespace (
    namespace_id             integer NOT NULL,
    prefix                   varchar(8) NULL,
    resource_location_oid    integer NOT NULL,
    PRIMARY KEY(namespace_id)
)
##
CREATE TABLE @.objects (
    objects_id         serial,
    type_id            integer NOT NULL,
    varchar_value      varchar(4000) NULL,
    objects_text_id    integer NULL,
    PRIMARY KEY(objects_id)
)
##
CREATE TABLE @.objects_text (
    objects_text_id     serial,
    text_value          text NOT NULL,
    PRIMARY KEY(objects_text_id)
)
##
CREATE TABLE @.objects_type (
    objects_type_id    integer NOT NULL,
    name               varchar(64) NOT NULL,
    PRIMARY KEY(objects_type_id)
)
##
CREATE TABLE @.relationship_type (
    relationship_type_id    integer NOT NULL,
    name                    varchar(64) NOT NULL,
    PRIMARY KEY(relationship_type_id)
)
##
CREATE TABLE @.statement (
    statement_id                integer NOT NULL,
    document_id                 integer NOT NULL,
    subject_term_id             integer NOT NULL,
    relationship_type_id        integer NULL,
    object_term_id              integer NULL,
    nested_subject_id           integer NULL,
    nested_relationship_type_id integer NULL,
    nested_object_id            integer NULL,
    PRIMARY KEY(statement_id)
)
##
CREATE TABLE @.kam_edge_statement_map (
    kam_edge_statement_id    serial,
    kam_edge_id              integer NOT NULL,
    statement_id             integer NOT NULL,
    PRIMARY KEY(kam_edge_statement_id)
)
##
CREATE TABLE @.statement_annotation_map (
    statement_annotation_id    serial,
    statement_id               integer NOT NULL,
    annotation_id              integer NOT NULL,
    PRIMARY KEY(statement_annotation_id)
)
##
CREATE TABLE @.term (
    term_id           integer NOT NULL,
    kam_node_id       integer NOT NULL,
    term_label_oid    integer NOT NULL,
    PRIMARY KEY(term_id)
)
##
CREATE TABLE @.term_parameter (
    term_parameter_id       integer NOT NULL,
    kam_global_parameter_id integer NOT NULL,
    term_id                 integer NOT NULL,
    namespace_id            integer NULL,
    parameter_value_oid     integer NOT NULL,
    ordinal                 integer NOT NULL,
    PRIMARY KEY(term_parameter_id)
)
##