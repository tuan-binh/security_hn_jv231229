package com.ra.service;

import com.ra.dto.request.FormLogin;
import com.ra.dto.request.FormRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.exception.CustomException;

public interface IAuthService {
	
	JwtResponse handleLogin(FormLogin formLogin) throws CustomException;
	
	void handleRegister(FormRegister formRegister);
	
}
