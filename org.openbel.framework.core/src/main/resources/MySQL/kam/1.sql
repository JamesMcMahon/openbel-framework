ALTER TABLE @.statement_annotation_map
	DROP FOREIGN KEY a_sa_fk
##
ALTER TABLE @.document_annotation_def_map
	DROP FOREIGN KEY ad_dad_fk
##
ALTER TABLE @.annotation
	DROP FOREIGN KEY ad_a_fk
##
ALTER TABLE @.annotation_definition
	DROP FOREIGN KEY ad_adt_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY dhi_s_fk
##
ALTER TABLE @.document_namespace_map
	DROP FOREIGN KEY d_dn_fk
##
ALTER TABLE @.document_annotation_def_map
	DROP FOREIGN KEY dhi_dad_fk
##
ALTER TABLE @.kam_node
	DROP FOREIGN KEY ft_kn_fk
##
ALTER TABLE @.kam_edge_statement_map
    DROP FOREIGN KEY ke_kas_fk
##
ALTER TABLE @.kam_node_parameter
	DROP FOREIGN KEY kn_knp_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY kn_kes_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY kn_ket_fk
##
ALTER TABLE @.term
	DROP FOREIGN KEY kn_t_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY knp_tp_fk
##
ALTER TABLE @.kam_node_parameter
    DROP FOREIGN KEY kpu_knp_fk
##
ALTER TABLE @.term_parameter
    DROP FOREIGN KEY kpu_tp_fk
##
ALTER TABLE @.document_namespace_map
    DROP FOREIGN KEY n_dn_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY n_p_fk
##
ALTER TABLE @.annotation
	DROP FOREIGN KEY o_a_n_fk
##
ALTER TABLE @.annotation_definition
	DROP FOREIGN KEY o_ad_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY o_p_fk
##
ALTER TABLE @.namespace
	DROP FOREIGN KEY o_n_fk
##
ALTER TABLE @.term
    DROP FOREIGN KEY o_t_fk
##
ALTER TABLE @.kam_node
    DROP FOREIGN KEY o_kn_fk
##
ALTER TABLE @.objects
	DROP FOREIGN KEY otx_o_fk
##
ALTER TABLE @.objects
    DROP FOREIGN KEY oty_o_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY rt_ke_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY rt_srt_fk
##
ALTER TABLE @.statement
    DROP FOREIGN KEY rt_snr_fk
##
ALTER TABLE @.statement_annotation_map
	DROP FOREIGN KEY s_sa_fk
##
ALTER TABLE @.kam_edge_statement_map
    DROP FOREIGN KEY s_kes_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY t_sst_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY t_sot_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY t_tp_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY t_sns_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY t_sno_fk
##
DROP TABLE @.annotation
##
DROP TABLE @.annotation_definition
##
DROP TABLE @.annotation_definition_type
##
DROP TABLE @.document_header_information
##
DROP TABLE @.document_annotation_def_map
##
DROP TABLE @.document_namespace_map
##
DROP TABLE @.function_type
##
DROP TABLE @.kam_edge
##
DROP TABLE @.kam_edge_statement_map
##
DROP TABLE @.kam_node
##
DROP TABLE @.kam_node_parameter
##
DROP TABLE @.kam_parameter_uuid
##
DROP TABLE @.namespace
##
DROP TABLE @.objects
##
DROP TABLE @.objects_text
##
DROP TABLE @.objects_type
##
DROP TABLE @.relationship_type
##
DROP TABLE @.statement
##
DROP TABLE @.statement_annotation_map
##
DROP TABLE @.term
##
DROP TABLE @.term_parameter
##
CREATE TABLE @.annotation (
    annotation_id                 int(15) NOT NULL,
    value_oid                     int(15) NOT NULL,
    annotation_definition_id      int(15) NOT NULL,
    PRIMARY KEY(annotation_id)
)
##
CREATE TABLE @.annotation_definition (
	annotation_definition_id     	int(15) NOT NULL,
	name                         	varchar(255) NOT NULL,
	description                  	varchar(255),
	annotation_usage               	varchar(255),
	domain_value_oid             	int(15) NOT NULL,
	annotation_definition_type_id	int(15) NOT NULL,
	PRIMARY KEY(annotation_definition_id)
)
##
CREATE TABLE @.annotation_definition_type (
    annotation_definition_type_id    int(15) NOT NULL,
    name                             varchar(64) NOT NULL,
    PRIMARY KEY(annotation_definition_type_id)
)
##
CREATE TABLE @.document_header_information (
    document_id         int(15) NOT NULL,
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
    document_annotation_definition    int(15) AUTO_INCREMENT NOT NULL,
    document_id                       int(15) NOT NULL,
    annotation_definition_id          int(15) NOT NULL,
    PRIMARY KEY(document_annotation_definition)
)
##
CREATE TABLE @.document_namespace_map (
    document_namespace_id    int(15) AUTO_INCREMENT NOT NULL,
    document_id              int(15) NOT NULL,
    namespace_id             int(15) NOT NULL,
    PRIMARY KEY(document_namespace_id)
)
##
CREATE TABLE @.function_type (
    function_type_id    int(15) NOT NULL,
    name                varchar(64) NOT NULL,
    PRIMARY KEY(function_type_id)
)
##
CREATE TABLE @.kam_edge (
    kam_edge_id             int(15) NOT NULL,
    kam_source_node_id      int(15) NOT NULL,
    kam_target_node_id      int(15) NOT NULL,
    relationship_type_id    int(15) NOT NULL,
    PRIMARY KEY(kam_edge_id)
)
##
CREATE TABLE @.kam_node (
    kam_node_id         int(15) NOT NULL,
    function_type_id    int(15) NOT NULL,
    node_label_oid      int(15) NOT NULL,
    PRIMARY KEY(kam_node_id)
)
##
CREATE TABLE @.kam_node_parameter (
    kam_node_parameter_id    int(15) AUTO_INCREMENT NOT NULL,
    kam_global_parameter_id  int(15) NOT NULL,
    kam_node_id              int(15) NOT NULL,
    ordinal                  int(15) NOT NULL,
    PRIMARY KEY(kam_node_parameter_id)
)
##
CREATE TABLE @.kam_parameter_uuid  ( 
    kam_parameter_uuid_id    int(15) AUTO_INCREMENT NOT NULL,
    kam_global_parameter_id  int(15) NOT NULL,
    most_significant_bits    bigint NOT NULL,
    least_significant_bits   bigint NOT NULL,
    PRIMARY KEY(kam_parameter_uuid_id)
)
##
CREATE TABLE @.namespace (
    namespace_id             int(15) NOT NULL,
    prefix                   varchar(8) NULL,
    resource_location_oid    int(15) NOT NULL,
    PRIMARY KEY(namespace_id)
)
##
CREATE TABLE @.objects (
    objects_id         int(15) AUTO_INCREMENT NOT NULL,
    type_id            int(15) NOT NULL,
    varchar_value      varchar(4000) NULL,
    objects_text_id    int(15) NULL,
    PRIMARY KEY(objects_id)
)
##
CREATE TABLE @.objects_text (
    objects_text_id    int(15) AUTO_INCREMENT NOT NULL,
    text_value          longtext NOT NULL,
    PRIMARY KEY(objects_text_id)
)
##
CREATE TABLE @.objects_type (
    objects_type_id    int(15) NOT NULL,
    name              varchar(64) NOT NULL,
    PRIMARY KEY(objects_type_id)
)
##
CREATE TABLE @.relationship_type (
    relationship_type_id    int(15) NOT NULL,
    name                    varchar(64) NOT NULL,
    PRIMARY KEY(relationship_type_id)
)
##
CREATE TABLE @.statement (
    statement_id                int(15) NOT NULL,
    document_id                 int(15) NOT NULL,
    subject_term_id             int(15) NOT NULL,
    relationship_type_id        int(15) NULL,
    object_term_id              int(15) NULL,
    nested_subject_id           int(15) NULL,
    nested_relationship_type_id int(15) NULL,
    nested_object_id            int(15) NULL,
    PRIMARY KEY(statement_id)
)
##
CREATE TABLE @.kam_edge_statement_map (
    kam_edge_statement_id    int(15) AUTO_INCREMENT NOT NULL,
    kam_edge_id              int(15) NOT NULL,
    statement_id             int(15) NOT NULL,
    PRIMARY KEY(kam_edge_statement_id)
)
##
CREATE TABLE @.statement_annotation_map (
    statement_annotation_id    int(15) AUTO_INCREMENT NOT NULL,
    statement_id               int(15) NOT NULL,
    annotation_id              int(15) NOT NULL,
    PRIMARY KEY(statement_annotation_id)
)
##
CREATE TABLE @.term (
    term_id           int(15) NOT NULL,
    kam_node_id       int(15) NOT NULL,
    term_label_oid    int(15) NOT NULL,
    PRIMARY KEY(term_id)
)
##
CREATE TABLE @.term_parameter (
    term_parameter_id       int(15) NOT NULL,
    kam_global_parameter_id int(15) NOT NULL,
    term_id                 int(15) NOT NULL,
    namespace_id            int(15) NULL,
    parameter_value_oid     int(15) NOT NULL,
    ordinal                 int(15) NOT NULL,
    PRIMARY KEY(term_parameter_id)
)
##