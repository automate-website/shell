package website.automate.shell.support;

import org.springframework.stereotype.Service;

import website.automate.manager.api.client.model.Authentication;

@Service
public class ShellContextHolder {

    private Authentication principal;

    public Authentication getPrincipal() {
        return principal;
    }

    public void setPrincipal(Authentication principal) {
        this.principal = principal;
    }
}
