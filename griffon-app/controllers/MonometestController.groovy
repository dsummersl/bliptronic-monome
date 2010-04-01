import sky.monome.*
import java.awt.event.*
import sky.monome.behavior.Toggle
import sky.monome.event.button.ButtonListener
import sky.monome.event.button.ButtonEvent
import sky.monome.event.analog.AnalogEvent
import sky.monome.event.analog.AnalogListener

class MonometestController {
    // these will be injected by Griffon
    def model
    def view

    Monome monome

    void mvcGroupInit(Map args) {
        // this method is called after model and view are injected
    }

    def executeScript(ActionEvent evt = null) {
        model.enabled = false
        doOutside {
            println "starting..."
            try {
                monome=new Monome("Monome",sky.monome.Monome.MonomeSize.MONOME_64,"localhost","/40h",8000,8080);
                monome.clear()
                monome.refresh()
                post()
                println "got it"
                edt {
                    model.monomeStartup = "Success!"
//                    model.enabled = true
                }
            }
            catch  (Exception e) {
                println "failed"
                e.printStackTrace()
                edt {
                    model.monomeStartup = e.getMessage()
                }
            }
        }
    }

    def toggleButton(button) {
        println "toggling"
        doOutside {
            monome.getComponents().each { c ->
                if (c instanceof LedButtonCouple) {
                    if (c.getName().toString().equals(button.toString())) {
                        println "clicking on ${c.getName()}"
                        c.doClick()
                        monome.refresh()
                    }
                }
            }
        }
    }

    def post() {
        (0..7).each { final int i->
            (0..7).each { final int j->
                final LedButtonCouple couple = new LedButtonCouple("${i},${j}",monome,j,i,new Toggle())
                couple.addButtonListener({ ButtonEvent<LedButtonCouple> evt ->
                    println "button released: ${evt.getButtonAction()} and its enabled? ${couple.getLedState()}"
                    if (evt.getButtonAction().equals(ButtonEvent.ButtonAction.BUTTON_RELEASED)) {
                        edt {
                            model.buttonStatus["${i},${j}".toString()] = couple.getLedState() == LedButtonCouple.LedState.ON ? true:false
                        }
                    }
                } as ButtonListener)
                monome.addComponent(couple)
            }
        }
        def a = new Analog("topdial",monome,0)
        a.addAnalogListener({ AnalogEvent evt ->
//            println "analog change to ${evt.getSource().getName()} to ${evt.getValue()}"
            model.topdial = evt.getValue() * 1024
        } as AnalogListener)
        monome.addComponent(a)
        a = new Analog("midtopdial",monome,1)
        a.addAnalogListener({ AnalogEvent evt ->
//            println "analog change to ${evt.getSource().getName()} to ${evt.getValue()}"
            model.midtopdial = evt.getValue() * 1024
        } as AnalogListener)
        monome.addComponent(a)
        a = new Analog("midbottomdial",monome,2)
        a.addAnalogListener({ AnalogEvent evt ->
//            println "analog change to ${evt.getSource().getName()} to ${evt.getValue()}"
            model.midbottomdial = evt.getValue() * 1024
        } as AnalogListener)
        monome.addComponent(a)
        a = new Analog("bottomdial",monome,3)
        a.addAnalogListener({ AnalogEvent evt ->
//            println "analog change to ${evt.getSource().getName()} to ${evt.getValue()}"
            model.bottomdial = evt.getValue() * 1024
        } as AnalogListener)
        monome.addComponent(a)

        monome.refresh()
    }
}