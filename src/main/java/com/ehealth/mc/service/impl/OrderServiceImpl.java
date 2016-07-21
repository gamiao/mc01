package com.ehealth.mc.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehealth.mc.bo.OrderHeader;
import com.ehealth.mc.dao.OrderHeaderDAO;
import com.ehealth.mc.service.OrderService;
import com.ehealth.mc.service.util.EntityUtil;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderHeaderDAO orderHeaderDAO;

	@Override
	public List<Entity> findAll() {
		
		List<Entity> eList = new ArrayList<Entity>();
		if (orderHeaderDAO != null) {

			Iterable<OrderHeader> result = orderHeaderDAO.findAll();
			if (result != null) {
				Iterator<OrderHeader> i = result.iterator();
				while (i.hasNext()) {
					Entity e = EntityUtil.getEntity(i.next());
					eList.add(e);
				}
				return eList;
			}
		}
		return null;
	}

}
