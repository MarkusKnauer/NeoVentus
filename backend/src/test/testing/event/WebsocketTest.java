package testing.event;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import testing.AbstractTest;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

/**
 * testing websocket
 *
 * @author Dennis Thanner
 */
public class WebsocketTest extends AbstractTest {

	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DeskRepository deskRepository;

	@LocalServerPort
	private int port;

	private String WEBSOCKET_URI;
	private String WEBSOCKET_TOPIC;

	BlockingQueue<String> blockingQueue;
	WebSocketStompClient stompClient;

	@Before
	public void setup() {
		blockingQueue = new LinkedBlockingDeque<>();
		stompClient = new WebSocketStompClient(new SockJsClient(
			asList(new WebSocketTransport(new StandardWebSocketClient()))));
		WEBSOCKET_URI = "ws://localhost:" + port + "/socket/socket-api";

	}

	/**
	 * testing send data for kitchen or bar after order updates
	 *
	 * @throws Exception
	 */
	@Test
	public void receiveOrderUpdateFromSocket() throws Exception {
		WEBSOCKET_TOPIC = "/topic/order/kitchen";

		MenuItemCategory menuItemCategory = new MenuItemCategory();
		menuItemCategory.setForKitchen(true);
		menuItemCategory = this.menuItemCategoryRepository.save(menuItemCategory);

		MenuItem m = new MenuItem();
		m.setMenuItemCategory(menuItemCategory);
		m = this.menuItemRepository.save(m);

		StompSession session = stompClient
			.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
			})
			.get(1, SECONDS);
		session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

		OrderItem o = new OrderItem();
		o.setItem(m);
		this.orderItemRepository.save(o);

		Assert.assertNotNull(blockingQueue.poll(3, SECONDS));
	}

	//fixme
//	@Test
//	public void testSingleNotificationSend() throws Exception {
//		User u = new User();
//		u.setUsername("Test");
//		u = this.userRepository.save(u);
//		WEBSOCKET_TOPIC ="/topic/notification/" + u.getId();
//
//		Desk d = new Desk();
//		d = this.deskRepository.save(d);
//
//		MenuItem m = new MenuItem();
//		m.setName("Schnitzel");
//		m = this.menuItemRepository.save(m);
//
//
//		StompSession session = stompClient.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
//		}).get(1, SECONDS);
//		session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
//
//		OrderItem o = new OrderItem();
//		o.setWaiter(u);
//		o.setDesk(d);
//		o.setItem(m);
//		o = this.orderItemRepository.save(o);
//
//		OrderItemState state = new OrderItemState(OrderItemState.State.CANCELED);
//		o.getStates().add(state);
//		// save should trigger cancle notification
//		o = this.orderItemRepository.save(o);
//
//		// check if message gets added to queue
//		Assert.assertNotNull(blockingQueue.poll(3, SECONDS));
//		blockingQueue.clear();
//
//		o = this.orderItemRepository.save(o);
//
//		Assert.assertNull(blockingQueue.poll(3, SECONDS));
//	}


	/**
	 * clean up
	 */
	@After
	public void clean() {
		this.orderItemRepository.deleteAll();
		this.menuItemRepository.deleteAll();
		this.menuItemCategoryRepository.deleteAll();
		this.userRepository.deleteAll();
	}

	class DefaultStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object o) {
			blockingQueue.offer(new String((byte[]) o));
		}
	}

}
