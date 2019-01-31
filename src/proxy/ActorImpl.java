package proxy;

public class ActorImpl implements IActor {
    @Override
    public void basicAct(float money) {
        System.out.println("基本表演 ： " + money);
    }

    @Override
    public void dangrtAct(float money) {
        System.out.println("危险表演 ： " + money);
    }
}
