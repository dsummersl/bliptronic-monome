import groovy.beans.Bindable
import sky.monome.Monome

class MonometestModel {
    @Bindable String monomeStartup
    @Bindable boolean enabled = true

    @Bindable Map buttonStatus = new ObservableMap()

    @Bindable int topdial = 0
    @Bindable int midtopdial = 0
    @Bindable int midbottomdial = 0
    @Bindable int bottomdial = 0
}