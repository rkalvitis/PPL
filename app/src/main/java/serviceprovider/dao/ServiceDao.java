package serviceprovider.dao;

import serviceprovider.models.Service;

public interface ServiceDao {
    Service getServiceProvider(int id);
    Service findServiceByEmail(String email);
    Service findRegisteredService(String email, String password);
    boolean createServiceProvider(Service service);
    boolean updateServiceProvider(Service service);
    boolean updatePassword(int userId, String newPassword);
    boolean deleteServiceProvider(int id);
}
