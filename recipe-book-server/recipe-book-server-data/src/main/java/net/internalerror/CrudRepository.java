package net.internalerror;

import org.jooq.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class CrudRepository<R extends UpdatableRecord<R>, C> {

    private final DSLContext dslContext;

    private final Table<R> table;

    private final TableField<R, C> idField;

    private final Class<C> idClazz;

    protected CrudRepository(DSLContext dslContext, Table<R> table, TableField<R, C> idField, Class<C> idClazz) {
        this.dslContext = dslContext;
        this.table = table;
        this.idField = idField;
        this.idClazz = idClazz;
    }

    public C insert(Function<R, R> mapping) {
        return Objects.requireNonNull(dslContext.insertInto(table).set(mapping.apply(dslContext.newRecord(table))).returningResult(idField).fetchOne()).into(idClazz);
    }

    public R get(C id) {
        return get(idField.eq(id));
    }

    public R get(Condition condition) {
        return dslContext.selectFrom(table).where(condition).fetchOne();
    }

    public List<R> read() {
        return read(idField.isNotNull());
    }

    public List<R> read(Condition condition) {
        return read(condition, idField);
    }

    public List<R> read(OrderField<?>... orderFields) {
        return read(idField.isNotNull(), orderFields);
    }

    public List<R> read(Condition condition, OrderField<?>... orderFields) {
        return dslContext.selectFrom(table).where(condition).orderBy(orderFields).fetch();
    }

    public boolean exists(C id) {
        return exists(idField.eq(id));
    }

    public boolean exists(Condition condition) {
        return dslContext.fetchExists(table, condition);
    }

    public void update(Function<R, R> mapping, Condition condition) {
        List<R> records = read(condition);
        for (R rec : records) {
            mapping.apply(rec).update();
        }
    }

    public void delete(C id) {
        delete(idField.eq(id));
    }

    public void delete(Condition condition) {
        dslContext.deleteFrom(table).where(condition).execute();
    }

}
