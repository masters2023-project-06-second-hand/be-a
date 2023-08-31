package com.codesquad.secondhand;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.codesquad.secondhand.annotation.IntegrationTest;
import com.codesquad.secondhand.domain.category.service.CategoryService;
import com.codesquad.secondhand.domain.image.service.ImageService;
import com.codesquad.secondhand.domain.jwt.Jwt;
import com.codesquad.secondhand.domain.jwt.JwtProvider;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.codesquad.secondhand.domain.product.repository.ImageJpaRepository;
import com.codesquad.secondhand.domain.product.service.ProductService;
import com.codesquad.secondhand.redis.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@IntegrationTest
public abstract class BaseControllerTest {

	public static final long MEMBER_ID = 1L;
	public static final String AUTHORIZATION = "Authorization";
	public static final String JWT_TOKEN_PREFIX = "Bearer ";
	public static final String TEST_EMAIL = "test@test.com";

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public ObjectMapper objectMapper;

	@Autowired
	public JwtProvider jwtProvider;

	@Autowired
	public CategoryService categoryService;

	@Autowired
	public ImageService imageService;

	@Autowired
	public MemberService memberService;

	@Autowired
	public ProductService productService;

	@Autowired
	public ImageJpaRepository imageJpaRepository;

	public Jwt jwt;

	@Autowired
	public RedisUtil redisUtil;

	@BeforeEach
	void init() {
		jwt = jwtProvider.createTokens(Map.of("memberId", MEMBER_ID, "email", TEST_EMAIL));
	}
}
