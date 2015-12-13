package reactor;

public class Tweet5sCounter implements Runnable {

	private Statistics Statistics;
	private int NumOfCounterRunnningInTheStart;
	
	public Tweet5sCounter(Statistics s,int num){
		this.Statistics=s;
		this.NumOfCounterRunnningInTheStart=num;
	}
	@SuppressWarnings("static-access")
	public void run() {
		try {Thread.currentThread().sleep(5000);}
		catch (InterruptedException e) {System.out.println("not possible, counter was interput");}
		int num=this.Statistics.getNumOfCounters()-this.NumOfCounterRunnningInTheStart;
		this.Statistics.updateTweeterCounter();
		this.Statistics.updateMaxTweetPer5s(num);
		this.Statistics.updateSumOfAllCounterResults(num);
	}

}
