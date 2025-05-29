package org.ezcode.codetest.application.chatting.service;

public class ChatFacade {


}

// 이건 또 무슨 신개념??
// 조합? 그런 느낌입니다 -> 서비스의 묶음??
// 게시물이면 post, comment??
//

//
 // JpaRepository -> JpaRepository 구현하는 클래스가 port interface를 implements 받아야 하나요?? 멤법 ㅕㄴ수로
//  port interface를 implements 받고, 그 클래스에서 JpaRepository 를 멤버 변수로 주입 받아서 save() return jpaRepo.save(Entity)
/*

//도메인 서비스는 ? (주입 받는 멤버변수가 존재하지 않는다고 생각하면 편하실 듯???)

//해당 도메인에 대한 행위, 상태변화 등등 작업만 하면 되고
// 어플리케이션 서비스는  transaction을 열어줘서 데이터를 로드/영속화 하거나
그리고 도메인 하나만으로는 실제 비즈니스
시나리오 구현이 안되기 때문에 여러 도메인을 불러와서 조합해서 실제 사용자의 비즈니스 시나리오를 만드는 계층
이라고 보시면 될거 같아요.

@Tra
public ~~~

User user = findBy

user.minus();

userDomainService.minusPoint(user, totalPrice);

if (user.getPoint < totalPrice) {
	throw ~~
}
user.minusPoint(totalPrice); << 얘는 엔티티 안에

꼬리무는 getter, 값을 변경하느 ㄴ거?

public void minusPoint() {
this.point -= point
}

userDomainService.save(dto)

++ 해당 도메인서비스에서는 해당 도메인 레포를 주입 받아서 save 그런 것들을 다 만들었어요.
from b
save
해당 도메인을 관장하는 util, support 클래스 느낌?

controller -> appService -> domain <- infra  <--- 4

controller -> appService -> domain
                port <- repo
@Service
public class applicationService {applicationServiceapplicationService

	PostDomainService;~~
	LikeDomainService;
	Repo  < ---

	PostDomainSerivce.addComment(Post,Comment);



	어플리케이션 서비스는  transaction을 열어줘서 데이터를 로드/영속화 하거나
그리고 도메인 하나만으로는 실제 비즈니스
시나리오 구현이 안되기 때문에 여러 도메인을 불러와서 조합해서 실제 사용자의 비즈니스 시나리오를 만드는 계층


}

   DB <--- 데이터를 로딩하고/영속화 하는 작업

// 울고싶다.영혼탈출
@Component
public class PostDomainService {

	public AddComment(Post post, Comment comment) { ddd
		if(Post.isDeleted) {

			예외~~~

		}

		if(~~~) {
			예외~~
		}

		if(무슨무슨 조건일 때) {

			comment.addLike();
		}
		post.addComment(comment);
		comment.addPost(post);
	}

}

자자 빨리 개발시작합시다. ?? 누구세요
domain service : 순수 자바 코드, repository 의존성 X



GPT 예시
주문 하나의 기능 덩치가 커지니까
재고 확인, 주문 생성, 결제 처리하는 서비스 쪼개고 이걸 불러옴
@Service
public class OrderFacade {   <--- 너무 무거워지면 채택 아니면 단일 서비스로 간단하게

    private final OrderService orderService; <<
    private final InventoryService inventoryService; <<
    private final PaymentService paymentService; <<

    @Autowired
    public OrderFacade(OrderService orderService,
                       InventoryService inventoryService,
                       PaymentService paymentService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderResponseDto placeOrder(PlaceOrderRequestDto req) {
        // 1) 재고 확인
        inventoryService.checkStock(req.getItems());

        // 2) 주문 생성
        Order order = orderService.createOrder(req.toOrder());

        // 3) 결제 처리
        PaymentResult result = paymentService.process(order, req.getPaymentInfo());

        // 4) 응답 DTO 조립
        return OrderResponseDto.from(order, result);
    }
}

 */

/*

*/