// the Views: determine ordering of button and label with boolean 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 

class HView extends HLayout implements IHDisplay {
    public HView(String name) {
		super(name);

		// adapt IListener to ActionListener
		doneb.addActionListener
		    (new ActionListener() {
			    // pre: (null != l)
			    // post: (null == l)
			    public void actionPerformed(ActionEvent e) {
					l.done(); 
					l = null;
				}
			});
		rollb.addActionListener
		    (new ActionListener() {
			    // pre: (null != l)
			    // post: (null == l)
			    public void actionPerformed(ActionEvent e) {
					sump.setText(sums(l.roll()));
					l = null;
			    }
			});
		skipb.addActionListener
		    (new ActionListener() {
			    // pre: (null != l)
			    // post: (null == l)
			    public void actionPerformed(ActionEvent e) {
					l.skip();
					l = null;
			    }
			}); 
	}

    // ------------------------------------------------------------------
    // TODO: allocate the right number of booleans programmatically
	private boolean[] actionEnabled = { true, true, true, true };

    private IListener l; 
    public void listen(IListener ll) {
		int x=0;
		l = ll; 

		for (Turn.Action action : Turn.Action.values()) {
			if (actionEnabled[action.ordinal()]) {
				setActionButtonEnabled(action, true);
			}
		}

		// wait until one of the buttons has been clicked 
		while (l != null) {
		    System.out.print("");
		}
		setEnabledAll(false);
    }

    public void sumDisplay(int s) {
		sump.setText(sums(s));
    }

    public void msgDisplay(String s) {
		msgp.setText(s);
    }

    public void setActionEnabled(Turn.Action action, boolean enabled) {
    	String actionString;
    	if (action == Turn.Action.SKIP) actionString = "skip";
    	else if (action == Turn.Action.HOLD) actionString = "hold";
    	else actionString = "roll";

    	System.out.println("Setting action " + actionString + " to be " + (enabled ? "enabled" : "disabled"));

    	actionEnabled[action.ordinal()] = false;
    }

    private void setActionButtonEnabled(Turn.Action action, boolean enabled) {
    	if (action == Turn.Action.SKIP) {
    		skipb.setEnabled(enabled);
    	} else if (action == Turn.Action.HOLD) {
    		doneb.setEnabled(enabled);
    	} else if (action == Turn.Action.ROLL) {
    		rollb.setEnabled(enabled);
    	} else if (action == Turn.Action.ROLLTWICE) {
    		roll2xb.setEnabled(enabled);
    	}
    }

    // ------------------------------------------------------------------
    // Examples 

    static HView v;

    // the simulated Human player 
    static boolean endtest;
    static int x;
    static int y;
    static Thread human;

    // for testing: 
    // Java doesn't understand real closures:
    static int skip; 
    static int done; 
    static int roll; 

    static public void createExamples() {
		v = new HView("test hview");	
		endtest = false;
		x = 0; 
		y = 0; 
		skip = 0; 
		done = 0; 
		roll = 0; 
		human = new Thread () {
			public void run() {
			    Robot r;
		    
			    try {
					r = new Robot(); 
					r.setAutoDelay(500);
				
					while (!endtest) {
				    	press(r,x,y);
					}

					v.quit(r);
				} catch (AWTException e) {
					System.out.println("awt exception thrown"); 
			    };
			}
		};
    }

    // ------------------------------------------------------------------
    // TEST

    public static void main(String argv[]) {

		IListener ll = 
		    new IListener() {	    
			    // int sum = 0; 
			    
			    public void skip() {
					skip += 1; 
			    }

			    public void done() {
					done += 1; 
			    }
		    
			    public int roll() {
					roll += 1; 
					return roll; 
			    }
			};
		
		human.start();
		
		test_button(ROLLX,ROLLY,ll);
		Tester.check(roll == 1,"roll button check"); 

		test_button(SKIPX,SKIPY,ll); 
		Tester.check(skip == 1,"skip button check"); 

		test_button(DONEX,DONEY,ll); 
		Tester.check(done == 1,"done button check"); 

		endtest = true;
    }

    private static void test_button(int i, int j, IListener ll) {
		x = i; 
		y = j; 
		v.listen(ll); 
    }
}
