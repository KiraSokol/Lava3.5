public class Car implements Runnable {
    private static int CARS_COUNT = 0;
    private static boolean isWinner;
    private Race race;
    private int speed;
    private String name;

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            MainClass.countDownLatchRaceStart.countDown();
            MainClass.cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        MainClass.winnerLock.lock();
        if (!isWinner) {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> " + this.name + " - ПОБЕДИТЕЛЬ!!!");
            isWinner = true;
        }
        MainClass.winnerLock.unlock();
        MainClass.countDownLatchRaceEnd.countDown();
    }
}