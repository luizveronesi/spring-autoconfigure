package dev.luizveronesi.autoconfigure.security;

public interface IUserService {
    
	IUser findForAuthentication(IUser user);
}
