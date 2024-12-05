#include "UI.h"

UI::UI(ShoppingDB &db, std::ostream& os): db(db), os(os) {

}

std::ostream & UI::getOs() const {
    return os;
}
