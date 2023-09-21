package com.codesquad.secondhand;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.codesquad.secondhand.amazon.S3Uploader;
import com.codesquad.secondhand.domain.chat.service.ChatService;
import com.codesquad.secondhand.domain.image.service.ImageQueryService;
import com.codesquad.secondhand.domain.image.service.ImageService;
import com.codesquad.secondhand.domain.jwt.domain.Jwt;
import com.codesquad.secondhand.domain.jwt.domain.JwtProvider;
import com.codesquad.secondhand.domain.jwt.service.JwtQueryService;
import com.codesquad.secondhand.domain.jwt.service.JwtService;
import com.codesquad.secondhand.domain.member.service.MemberService;
import com.codesquad.secondhand.domain.member_region.service.MemberRegionService;
import com.codesquad.secondhand.domain.product.service.ProductService;
import com.codesquad.secondhand.domain.reaction.service.ReactionService;
import com.codesquad.secondhand.redis.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public MemberService memberService;

	@Autowired
	public MemberRegionService memberRegionService;

	@Autowired
	public ProductService productService;

	@Autowired
	public ImageService imageService;

	@MockBean
	public S3Uploader s3Uploader;

	@Autowired
	public ImageQueryService imageQueryService;

	@Autowired
	public ReactionService reactionService;

	@Autowired
	public JwtService jwtService;

	@Autowired
	public JwtQueryService jwtQueryService;

	public Jwt jwt;

	@Autowired
	public RedisUtil redisUtil;

	@Autowired
	public ChatService chatService;

	@BeforeEach
	void init() {
		jwt = jwtProvider.createTokens(Map.of("memberId", MEMBER_ID));
	}
}
