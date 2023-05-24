package serviceprovider.dao;

import serviceprovider.models.Service;

public interface ServiceDao {
    Service getServiceProvider(int id);
    boolean createServiceProvider(Service service);
    boolean updateServiceProvider(Service service);
    boolean deleteServiceProvider(int id);
}
