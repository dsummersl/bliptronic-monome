import javax.swing.*
import java.awt.event.*
import java.beans.PropertyChangeListener

application(title:'Monome',
  pack:true,
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-16x16.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]
) {
    def buttons = {}
    borderLayout()
    panel(constraints:CENTER) {
        def buttonSize = [22,22]
        boxLayout(axis:BoxLayout.Y_AXIS)
        (0..7).each { final int i->
            panel() {
                boxLayout(axis:BoxLayout.X_AXIS)
                (0..7).each { final int j ->
                    def b = button(preferredSize: buttonSize,maximumSize: buttonSize,minimumSize: buttonSize,
                        id: "${i},${j}".toString(),
                        actionPerformed:{ controller.toggleButton("${i},${j}")},
                        icon: imageIcon('/white.png')
                    )
                    buttons["${i},${j}".toString()] = b
                }
            }
        }
        model.buttonStatus.addPropertyChangeListener({ evt ->
            println "map changed ${evt.propertyName}"
            buttons["${evt.propertyName}"].setIcon(model.buttonStatus[evt.propertyName] ? imageIcon('/red.png'):imageIcon('/white.png'))
        } as PropertyChangeListener)
    }
    panel(constraints:EAST) {
        boxLayout(axis:BoxLayout.Y_AXIS)
        def sliderSize = [90,40]
        slider(id: "topdial", maximum: 1024, enabled: false, preferredSize: sliderSize, maximumSize: sliderSize, minimumSize: sliderSize, value: bind { model.topdial })
        slider(id: "middletop", maximum: 1024, enabled: false, preferredSize: sliderSize, maximumSize: sliderSize, minimumSize: sliderSize, value: bind { model.midtopdial })
        slider(id: "middlebottom", maximum: 1024, enabled: false, preferredSize: sliderSize, maximumSize: sliderSize, minimumSize: sliderSize, value: bind { model.midbottomdial })
        slider(id: "bottomdial", maximum: 1024, enabled: false, preferredSize: sliderSize, maximumSize: sliderSize, minimumSize: sliderSize, value: bind { model.bottomdial })
    }
    panel(constraints:SOUTH) {
        button("Start", actionPerformed:controller.&executeScript, enabled: bind {model.enabled})
        hstrut(5)
        label(text: bind{model.monomeStartup})
    }
}
