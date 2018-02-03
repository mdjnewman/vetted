package me.mdjnewman.vetted.importer

import org.hibernate.dialect.PostgreSQL95Dialect
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor
import java.sql.Types

class PostgreSQLDialectCustom : PostgreSQL95Dialect() {

    init {
        registerColumnType(Types.BLOB, "bytea")
    }

    override fun remapSqlTypeDescriptor(sqlTypeDescriptor: SqlTypeDescriptor): SqlTypeDescriptor {
        return if (sqlTypeDescriptor.sqlType == java.sql.Types.BLOB) {
            BinaryTypeDescriptor.INSTANCE
        } else super.remapSqlTypeDescriptor(sqlTypeDescriptor)
    }
}