interface IHDisplay extends IDisplay {

    // register a listener for button events, 
    // wait until one of the buttons has been clicked
    void listen(IListener ll);

    // set whether the button for an action should be enabled
    void setActionEnabled(Turn.Action action, boolean enabled);
}
