package ts.daoBase;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
/**
 * 提供hibernate dao的所有操作,<br>
 * 实现类由spring注入HibernateEntityDao和HibernateGenericDao来实现
 * 
 */
public interface IBaseDao<T,PK extends Serializable> {
	
    /**
     * 根据ID获取对象. 实际调用Hibernate的session.load()方法返回实体或其proxy对象. 如果对象不存在，抛出异常.
     */
    T get(PK id);

    /**
     * 获取全部对象
     * 
     */
    List<T> getAll();
    
    /**
     * 获取全部对象,带排序参数.
     */
    List<T> getAll(String orderBy, boolean isAsc);
    
    /**
     * 根据属性名和属性值查询对象.
     * 
     * @return 符合条件的对象列表
     */
    List<T> findBy(String orderBy, boolean isAsc, Criterion... criterions);
   
    /**
     * 根据属性名和属性值查询对象,带排序参数.
     * 
     * @return 符合条件的对象列表
     */
    List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc);
    
    List<T> findLike(String propertyName, Object value, String orderBy, boolean isAsc);
    
    /**
     * 保存对象.
     */
    void save(T entity);
    
    /**
     * 在不同的session中关联修改过的托管对象
     */
    void update(T entity);

    /**
     * 删除对象.
     */
    void remove(T entity);
    
    /**
     * 根据ID移除对象.
     */
    void removeById(PK id);
    
    /**
     * 消除与 Hibernate Session 的关联
     * 
     */
    void evict(T entity);
            
    void flush();
    
    void clear();

}
