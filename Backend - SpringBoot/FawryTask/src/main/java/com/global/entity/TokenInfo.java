package com.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@Column(length = 800)
	private String accessToken;
	
	@NotBlank
	@Column(length = 800)
	private String refreshToken;
	
	private String userAgentText;
	
	private String localIpAddress;
	
	private String remoteIpAddress;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "id")
	private User user;

	public TokenInfo(String accessToken, String refreshToken) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public TokenInfo(@NotBlank String accessToken, @NotBlank String refreshToken, String userAgentText,
			String localIpAddress, String remoteIpAddress, User user) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userAgentText = userAgentText;
		this.localIpAddress = localIpAddress;
		this.remoteIpAddress = remoteIpAddress;
		this.user = user;
	}
	
	
}