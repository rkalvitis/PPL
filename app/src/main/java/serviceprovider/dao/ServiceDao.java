package serviceprovider.dao;

import serviceprovider.models.Service;
import userAuthentication.models.User;

public interface ServiceDao {
    Service getServiceProvider(int id);
    Service findRegisteredService(String email, String password);
    boolean createServiceProvider(Service service);
    boolean updateServiceProvider(Service service);
    boolean deleteServiceProvider(int id);
}
