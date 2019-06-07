package exam.service.base;

import java.util.List;

import exam.dao.base.BaseDao;
import exam.model.page.PageBean;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected BaseDao<T> baseDao;
	
	/**
	 * 设置具体的BaseDao
	 */
	protected abstract void setBaseDao(BaseDao<T> baseDao);
	
	public List<T> findAll() {
		throw new UnsupportedOperationException();
	}
	
	public void delete(Object id) {
		throw new UnsupportedOperationException();
	}
	
	public void saveOrUpdate(T entity) {
		throw new UnsupportedOperationException();
	}
	
	public List<T> find(T entity) {
		return baseDao.find(entity);
	}
	
	public PageBean<T> pageSearch(int pageCode, int pageSize, int pageNumber,
			String where, List<Object> params, String orderbys) {
		return baseDao.pageSearch(pageCode, pageSize, pageNumber, where, params, orderbys);
	}
	
}
