public class Tester {
    public Tester() {
    }

    @Before
    public void setUp() {
        System.out.println("Выполнение setUp.");
    }

    @After
    public void tearDown() {
        System.out.println("Выполнение tearDown.\n");
    }

    @Test
    public void testFirst() throws Exception {
        throw new Exception("Ошибка в testFirst!!!");
    }

    @Test
    public void testSecond() throws Exception {
        throw new Exception("Ошибка в testSecond!!!");
    }

    @Test
    public void testThird() {
        System.out.println("Выполнение testThird.");
    }
}
