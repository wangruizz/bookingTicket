package ts.daoBase;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * 提供hibernate dao的所有操作 * 
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class BaseDao<T,PK extends Serializable> extends HibernateDaoSupport implements IBaseDao<T,PK> {
    protected Class<T> entityClass;			// DAO所管理的Entity类型.

    /**
     *让spring提供构造函数注入
     */
    public BaseDao(Class<T> type) {
        this.entityClass = type;
    }

    public BaseDao(){
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 获取指定对象
     * @param id 主键
     * @return 主键对应的对象
     */
    @Override
    public T get(PK id) {
        return getHibernateTemplate().get(getEntityClass(), id);
    }

    /**
     * 获取数据库中的全部对象
     * @return 全部对象
     */
    @Override
    public List<T> getAll() {
        return getHibernateTemplate().loadAll(getEntityClass());
    }

    /**
     * 获取安装某一属性排序后的全部对象
     * @param orderBy 属性名
     * @param isAsc true代表升序 false代表降序
     * @return 排序后的对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll(String orderBy, boolean isAsc) {
        Assert.hasText(orderBy);
        if (isAsc)
            return (List<T>) getHibernateTemplate().findByCriteria(
                    DetachedCriteria.forClass(getEntityClass()).addOrder(Order.asc(orderBy)));
        else
            return (List<T>) getHibernateTemplate().findByCriteria(
                    DetachedCriteria.forClass(getEntityClass()).addOrder(Order.desc(orderBy)));
    }

    /**
     * 按照指定条件查找，并返回排序后的结果
     * @param orderBy 排序的属性
     * @param isAsc true代表升序 false代表降序
     * @param criterions 限制条件
     * @return 满足条件的全部的对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findBy(String orderBy, boolean isAsc, Criterion... criterions) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        if (isAsc){
            criteria.addOrder(Order.asc(orderBy));
        } else{
            criteria.addOrder(Order.desc(orderBy));
        }
        return (List<T>) getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * 指定属性值相等的对象
     * @param propertyName 属性名
     * @param value 属性值
     * @param orderBy 排序的属性
     * @param isAsc true代表升序 false代表降序
     * @return 满足条件的全部对象
     */
    @Override
    public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc) {
        Assert.hasText(propertyName);
        Assert.hasText(orderBy);
        return findBy(orderBy, isAsc, Restrictions.eq(propertyName, value));
    }

    /**
     * 模糊查找
     * @param propertyName 属性名
     * @param value  属性值
     * @param orderBy 排序的属性名
     * @param isAsc true代表升序 false代表降序
     * @return 满足条件的全部对象
     */
    @Override
    public List<T> findLike(String propertyName, Object value, String orderBy, boolean isAsc) {
        Assert.hasText(propertyName);
        Assert.hasText(orderBy);
        return findBy(orderBy, isAsc, Restrictions.like(propertyName, value));
    }

    /**
     * 持久化对象
     * @param entity 对象
     */
    @Override
    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    /**
     * 更新持久化对象
     * @param entity 对象
     */
    @Override
    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    /**
     * 删除对象
     * @param entity 对象
     */
    @Override
    public void remove(T entity) {
        getHibernateTemplate().delete(entity);
    }

    /**
     * 删除指定主键的对象
     * @param id 主键
     */
    @Override
    public void removeById(PK id) {
        remove(get(id));
    }

    /**
     * 删除指定对象的缓存
     * @param entity
     */
    @Override
    public void evict(T entity) {
        getHibernateTemplate().evict(entity);
    }

    /**
     * 执行所有待执行的数据库操作
     */
    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    /**
     * 清空hibernate缓存
     */
    @Override
    public void clear() {
        getHibernateTemplate().clear();
    }

}