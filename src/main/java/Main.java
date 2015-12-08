import model.A;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

public class Main {
	private static final String CONFIG_FILE = "hibernate.cfg.xml";

	private SessionFactory createFactory() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
		.configure() // configures settings from hibernate.cfg.xml
		.build();

		return new MetadataSources( registry ).buildMetadata().buildSessionFactory();

	}

	public static void main(String[] args) {
		new Main().run();
	}

	public void run() {
		SessionFactory factory = createFactory();

		createInitialData(factory);

		System.out.println("Getting test case ");
		getCollection(factory);

	}

	private void getCollection(SessionFactory factory) {

		Session session = factory.openSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(A.class, "a");
		criteria.createAlias("a.b", "b", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("a.d", "d", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("b.c", "c", JoinType.LEFT_OUTER_JOIN, Restrictions.conjunction().add(Restrictions.eqProperty("c.code", "b.code")).add(Restrictions.eqProperty("c.month", "d.month")));

		criteria.list();

		session.getTransaction().commit();
		session.close();

	}

	private void createInitialData(SessionFactory factory) {
		Session session = factory.openSession();
		session.beginTransaction();

		session.getTransaction().commit();
		session.close();
	}

}
