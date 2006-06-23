/**
 * Copyright (c) 2000-2006 Liferay, LLC. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.polls.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.service.persistence.BasePersistence;

import com.liferay.portlet.polls.NoSuchChoiceException;

import com.liferay.util.StringPool;
import com.liferay.util.dao.hibernate.OrderByComparator;
import com.liferay.util.dao.hibernate.QueryUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="PollsChoicePersistence.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class PollsChoicePersistence extends BasePersistence {
	public com.liferay.portlet.polls.model.PollsChoice create(
		PollsChoicePK pollsChoicePK) {
		PollsChoiceHBM pollsChoiceHBM = new PollsChoiceHBM();
		pollsChoiceHBM.setNew(true);
		pollsChoiceHBM.setPrimaryKey(pollsChoicePK);

		return PollsChoiceHBMUtil.model(pollsChoiceHBM);
	}

	public com.liferay.portlet.polls.model.PollsChoice remove(
		PollsChoicePK pollsChoicePK)
		throws NoSuchChoiceException, SystemException {
		Session session = null;

		try {
			session = openSession();

			PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)session.get(PollsChoiceHBM.class,
					pollsChoicePK);

			if (pollsChoiceHBM == null) {
				_log.warn("No PollsChoice exists with the primary key " +
					pollsChoicePK.toString());
				throw new NoSuchChoiceException(
					"No PollsChoice exists with the primary key " +
					pollsChoicePK.toString());
			}

			session.delete(pollsChoiceHBM);
			session.flush();

			return PollsChoiceHBMUtil.model(pollsChoiceHBM);
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.polls.model.PollsChoice update(
		com.liferay.portlet.polls.model.PollsChoice pollsChoice)
		throws SystemException {
		Session session = null;

		try {
			if (pollsChoice.isNew() || pollsChoice.isModified()) {
				session = openSession();

				if (pollsChoice.isNew()) {
					PollsChoiceHBM pollsChoiceHBM = new PollsChoiceHBM();
					pollsChoiceHBM.setQuestionId(pollsChoice.getQuestionId());
					pollsChoiceHBM.setChoiceId(pollsChoice.getChoiceId());
					pollsChoiceHBM.setDescription(pollsChoice.getDescription());
					session.save(pollsChoiceHBM);
					session.flush();
				}
				else {
					PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)session.get(PollsChoiceHBM.class,
							pollsChoice.getPrimaryKey());

					if (pollsChoiceHBM != null) {
						pollsChoiceHBM.setDescription(pollsChoice.getDescription());
						session.flush();
					}
					else {
						pollsChoiceHBM = new PollsChoiceHBM();
						pollsChoiceHBM.setQuestionId(pollsChoice.getQuestionId());
						pollsChoiceHBM.setChoiceId(pollsChoice.getChoiceId());
						pollsChoiceHBM.setDescription(pollsChoice.getDescription());
						session.save(pollsChoiceHBM);
						session.flush();
					}
				}

				pollsChoice.setNew(false);
				pollsChoice.setModified(false);
			}

			return pollsChoice;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.polls.model.PollsChoice findByPrimaryKey(
		PollsChoicePK pollsChoicePK)
		throws NoSuchChoiceException, SystemException {
		Session session = null;

		try {
			session = openSession();

			PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)session.get(PollsChoiceHBM.class,
					pollsChoicePK);

			if (pollsChoiceHBM == null) {
				_log.warn("No PollsChoice exists with the primary key " +
					pollsChoicePK.toString());
				throw new NoSuchChoiceException(
					"No PollsChoice exists with the primary key " +
					pollsChoicePK.toString());
			}

			return PollsChoiceHBMUtil.model(pollsChoiceHBM);
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByQuestionId(String questionId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM WHERE ");

			if (questionId == null) {
				query.append("questionId is null");
			}
			else {
				query.append("questionId = ?");
			}

			query.append(" ");
			query.append("ORDER BY ");
			query.append("choiceId ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;

			if (questionId != null) {
				q.setString(queryPos++, questionId);
			}

			Iterator itr = q.list().iterator();
			List list = new ArrayList();

			while (itr.hasNext()) {
				PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)itr.next();
				list.add(PollsChoiceHBMUtil.model(pollsChoiceHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findByQuestionId(String questionId, int begin, int end)
		throws SystemException {
		return findByQuestionId(questionId, begin, end, null);
	}

	public List findByQuestionId(String questionId, int begin, int end,
		OrderByComparator obc) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM WHERE ");

			if (questionId == null) {
				query.append("questionId is null");
			}
			else {
				query.append("questionId = ?");
			}

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("choiceId ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;

			if (questionId != null) {
				q.setString(queryPos++, questionId);
			}

			List list = new ArrayList();
			Iterator itr = QueryUtil.iterate(q, getDialect(), begin, end);

			while (itr.hasNext()) {
				PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)itr.next();
				list.add(PollsChoiceHBMUtil.model(pollsChoiceHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public com.liferay.portlet.polls.model.PollsChoice findByQuestionId_First(
		String questionId, OrderByComparator obc)
		throws NoSuchChoiceException, SystemException {
		List list = findByQuestionId(questionId, 0, 1, obc);

		if (list.size() == 0) {
			String msg = "No PollsChoice exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "questionId=";
			msg += questionId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchChoiceException(msg);
		}
		else {
			return (com.liferay.portlet.polls.model.PollsChoice)list.get(0);
		}
	}

	public com.liferay.portlet.polls.model.PollsChoice findByQuestionId_Last(
		String questionId, OrderByComparator obc)
		throws NoSuchChoiceException, SystemException {
		int count = countByQuestionId(questionId);
		List list = findByQuestionId(questionId, count - 1, count, obc);

		if (list.size() == 0) {
			String msg = "No PollsChoice exists with the key ";
			msg += StringPool.OPEN_CURLY_BRACE;
			msg += "questionId=";
			msg += questionId;
			msg += StringPool.CLOSE_CURLY_BRACE;
			throw new NoSuchChoiceException(msg);
		}
		else {
			return (com.liferay.portlet.polls.model.PollsChoice)list.get(0);
		}
	}

	public com.liferay.portlet.polls.model.PollsChoice[] findByQuestionId_PrevAndNext(
		PollsChoicePK pollsChoicePK, String questionId, OrderByComparator obc)
		throws NoSuchChoiceException, SystemException {
		com.liferay.portlet.polls.model.PollsChoice pollsChoice = findByPrimaryKey(pollsChoicePK);
		int count = countByQuestionId(questionId);
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM WHERE ");

			if (questionId == null) {
				query.append("questionId is null");
			}
			else {
				query.append("questionId = ?");
			}

			query.append(" ");

			if (obc != null) {
				query.append("ORDER BY " + obc.getOrderBy());
			}
			else {
				query.append("ORDER BY ");
				query.append("choiceId ASC");
			}

			Query q = session.createQuery(query.toString());
			int queryPos = 0;

			if (questionId != null) {
				q.setString(queryPos++, questionId);
			}

			Object[] objArray = QueryUtil.getPrevAndNext(q, count, obc,
					pollsChoice, PollsChoiceHBMUtil.getInstance());
			com.liferay.portlet.polls.model.PollsChoice[] array = new com.liferay.portlet.polls.model.PollsChoice[3];
			array[0] = (com.liferay.portlet.polls.model.PollsChoice)objArray[0];
			array[1] = (com.liferay.portlet.polls.model.PollsChoice)objArray[1];
			array[2] = (com.liferay.portlet.polls.model.PollsChoice)objArray[2];

			return array;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public List findAll() throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM ");
			query.append("ORDER BY ");
			query.append("choiceId ASC");

			Query q = session.createQuery(query.toString());
			Iterator itr = q.iterate();
			List list = new ArrayList();

			while (itr.hasNext()) {
				PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)itr.next();
				list.add(PollsChoiceHBMUtil.model(pollsChoiceHBM));
			}

			return list;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public void removeByQuestionId(String questionId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM WHERE ");

			if (questionId == null) {
				query.append("questionId is null");
			}
			else {
				query.append("questionId = ?");
			}

			query.append(" ");
			query.append("ORDER BY ");
			query.append("choiceId ASC");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;

			if (questionId != null) {
				q.setString(queryPos++, questionId);
			}

			Iterator itr = q.list().iterator();

			while (itr.hasNext()) {
				PollsChoiceHBM pollsChoiceHBM = (PollsChoiceHBM)itr.next();
				session.delete(pollsChoiceHBM);
			}

			session.flush();
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	public int countByQuestionId(String questionId) throws SystemException {
		Session session = null;

		try {
			session = openSession();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) ");
			query.append(
				"FROM PollsChoice IN CLASS com.liferay.portlet.polls.service.persistence.PollsChoiceHBM WHERE ");

			if (questionId == null) {
				query.append("questionId is null");
			}
			else {
				query.append("questionId = ?");
			}

			query.append(" ");

			Query q = session.createQuery(query.toString());
			int queryPos = 0;

			if (questionId != null) {
				q.setString(queryPos++, questionId);
			}

			Iterator itr = q.list().iterator();

			if (itr.hasNext()) {
				Long count = (Long)itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (HibernateException he) {
			throw new SystemException(he);
		}
		finally {
			closeSession(session);
		}
	}

	private static Log _log = LogFactory.getLog(PollsChoicePersistence.class);
}