package HuntTheWumpus.fixtures;

import junit.framework.TestCase;

public class MockConsoleTest extends TestCase {
  private MockConsole mc;

  protected void setUp() throws Exception {
    mc = new MockConsole();
  }

  public void testCheckFailsOnNoMessage() throws Exception {
    assertFalse(mc.check("message"));
  }


  public void testCheckPassesWithOneMessage() {
    mc.print("message");
    assertTrue(mc.check("message"));
  }

  public void testCheckPassesWithThreeMessages() {
    mc.print("message one");
    mc.print("message two");
    mc.print("message three");
    assertTrue(mc.check("message three"));
    assertTrue(mc.check("message one"));
    assertTrue(mc.check("message two"));
    assertFalse(mc.check("message x"));
    assertFalse(mc.check("message"));
  }

  public void testListClearedBetweenCheckAndPrint() {
    mc.print("message one");
    assertTrue(mc.check("message one"));
    mc.print("message two");
    assertFalse(mc.check("message one"));
    assertTrue(mc.check("message two"));
  }
}
