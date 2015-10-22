package com.bankonet.dao;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.compte.CompteDao;

public interface DaoFactory {
	ClientDao getClientDao();
	CompteDao getCompteDao();
}
