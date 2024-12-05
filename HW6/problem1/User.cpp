#include <functional>
#include <algorithm>
#include "User.h"
#include "Membership.h"

int User::lastId = 0;

User::User(std::string name, std::string password)
: name(name), password(password), membership(new Normal()), totalPayment(0), id(lastId++) { }

User::~User() {
    delete membership;
}

float User::GetDiscountRate() const {
    return membership->getDiscountRate();
}

std::function<bool(User*, User*)> User::GetUserComparator() {
    return membership->compareUser(this);
}

void User::updateMembership() {
    if (totalPayment >= Membership::PREMIUM_THRESHOLD) {
        delete membership;
        membership = new Premium;
    }
}
