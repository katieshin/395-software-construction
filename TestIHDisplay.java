// a simplistic implementation of the IHDisplay interface 
class TestIHDisplay extends TestIDisplay implements IHDisplay {

    TestIHDisplay() { super(); }

    public void listen(IListener ll) {
		int i = (int)Math.floor(4 * Math.random()); 
		if (0 == i) 
		    i = 4; 
		// now 1 <= i <= 4

		if (1 == i)
		    ll.roll();
		else if (2 == i) 
		    ll.skip();
		else if (3 == i)
			ll.roll2x();
		else // (4 == i)
		    ll.done(); 
    }

    // a useful instance 
    static public IHDisplay testIDisplay = new TestIHDisplay();

    public void setActionEnabled(Turn.Action action, boolean enabled) {
    	return;
    }
}

