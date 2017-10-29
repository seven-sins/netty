package com.lonecpp.server.config;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author seven sins
 * @date 2017年10月22日 下午10:30:53
 */
@Component
public class ServerMain implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	public static volatile boolean RUNNING = true;
	private static final Logger LOGGER = Logger.getLogger(ServerMain.class);
	
	@Override
	public int getOrder() {
		// 启动顺序(数值越小越先执行)
		return 1;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		try{
			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run(){
					RUNNING = false;
					ServerMain.class.notify();
				}
			});
			synchronized(ServerMain.class){
				while(RUNNING){
					ServerMain.class.wait();
				}
			}
		}catch(Exception e){
			LOGGER.error("=============启动出错=============", e);
			System.exit(0);
		}
	}

}
