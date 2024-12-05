#include "Membership.h"
#include "User.h"

float Normal::getDiscountRate() const {
    return 0;
}

std::function<bool(User*, User*)> Normal::compareUser(User* user) {
    return [user](User* l, User* r){
        // TODO
        return true; // remove this after implementation
    };
}

float Premium::getDiscountRate() const {
    return 0.1;
}

std::function<bool(User*, User*)> Premium::compareUser(User* user) {
    return [user](User* l, User* r){
        // TODO
        return true; // remove this after implementation
    };
}

