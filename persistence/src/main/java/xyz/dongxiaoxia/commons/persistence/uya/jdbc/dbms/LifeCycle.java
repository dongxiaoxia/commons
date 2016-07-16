package xyz.dongxiaoxia.commons.persistence.uya.jdbc.dbms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface LifeCycle {

	public void check();
	
	public static class Dispatcher {
		
		final List<LifeCycle> lcs = new ArrayList<LifeCycle>();
		
		final static int period = 5000; // 5s
		
		public final static Dispatcher singleton = new Dispatcher();
		
		static{
			singleton.checkLiveTask.setDaemon(true);
			singleton.checkLiveTask.start();
		}
		
		
		private Dispatcher(){}
		
		public void register(LifeCycle lc){
			if (lc == null) return;
			if (!lcs.contains(lc))
				lcs.add(lc);
		}
		
		public void remove(LifeCycle lc){
			if (lc == null) return;
			if (lcs.contains(lc))
				lcs.remove(lc);
		}
		
		Executor executor = Executors.newCachedThreadPool();
		
		public void execute(Runnable command){
			executor.execute(command);
		}
		
	    /**
	     * 线程的run方法，它将和其他线程同时运行
	     */
		private Thread checkLiveTask = new Thread (){
			@Override
		    public void run() {
		    	while(true){
		    		try{
		    		Thread.sleep(period);
		    		for(LifeCycle lc : lcs)
		    			lc.check();
		    		} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		    }
		};
	}
}
