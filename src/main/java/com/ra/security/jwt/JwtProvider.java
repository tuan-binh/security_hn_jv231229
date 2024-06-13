package com.ra.security.jwt;

import com.ra.security.principal.UserDetailCustom;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
	@Value("${jwt.secret_key}")
	private String SECRET_KEY;
	@Value("${jwt.expired.access}")
	private Long EXPIRED_ACCESS;
	
	
	// tạo ra chuỗi token để gửi về client => sau khi muốn xác
	// thực cái gì thì gửi lên token kèm theo để biết người đó là ai
	public String generateToken(UserDetailCustom userDetailCustom) {
		// thành phần của jwt 3 phần
		// 1. header { alg: HS512,type: JWT }
		// 2. payload { subject: trường dữ liệu unique để biết được thằng đấy thằng nào }
		// 3. signature { chữ ký : secret key }
		return Jwts.builder()
				  .setSubject(userDetailCustom.getEmail())
				  .setIssuedAt(new Date())
				  .setExpiration(new Date(new Date().getTime() + EXPIRED_ACCESS))
				  .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				  .compact();
	}
	
	public boolean validationToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (UnsupportedJwtException ex) {
			log.error("Invalid JWT token {}", ex.getMessage());
		} catch (SignatureException ex) {
			log.error("Signature exception {}", ex.getMessage());
		} catch (MalformedJwtException ex) {
			log.error("Malformed URL exception {}", ex.getMessage());
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token {}", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty {}", ex.getMessage());
		}
		return false;
	}
	
	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}
	
}
