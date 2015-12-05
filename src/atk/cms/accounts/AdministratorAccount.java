package atk.cms.accounts;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

// Bean lives as long as the HTTP session lives
@ManagedBean(name="adminAccount")
@SessionScoped
public class AdministratorAccount extends Account {
    
    public AdministratorAccount() {	
    }
}