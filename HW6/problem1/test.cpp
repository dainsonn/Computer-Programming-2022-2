#include <iostream>
#include <sstream>
#include <fstream>
#include <algorithm>
#include "ShoppingDB.h"
#include "AdminUI.h"
#include "ClientUI.h"

#define AUTOGRADE_DIRPATH "test/"

void print_OX(std::string test_name, bool is_correct) {
    std::cout << test_name << " : " << (is_correct ? "O" : "X") << std::endl;
}

bool is_space(char ch) {
    return ch == ' ' || ch == '\n' || ch == '\r';
}

void remove_space(std::string& str) {
    str.erase(std::remove_if(str.begin(), str.end(), is_space), str.end());
}

void clear_ostream_as_ostringstream(std::ostream& os) {
    std::ostringstream& oss = dynamic_cast<std::ostringstream&>(os);
    oss.str("");
    oss.clear();
}

bool compare_output(UI& ui, std::string out_filename) {
    std::ifstream ifs(AUTOGRADE_DIRPATH + out_filename);
    std::ostringstream oss_answer;
    oss_answer << ifs.rdbuf();
    std::string output_answer = oss_answer.str();

    std::ostringstream& oss = dynamic_cast<std::ostringstream&>(ui.getOs());
    std::string output_app = oss.str();

    remove_space(output_answer);
    remove_space(output_app);

    return output_app == output_answer;
}

void test1(AdminUI& adminUI) {
#if MAIN
    std::cout << std::endl << "========== Test 1 ==========" << std::endl;
#endif
#if TEST
    clear_ostream_as_ostringstream(adminUI.getOs());
#endif

    adminUI.ListProduct();
    adminUI.AddProduct("tissue", 2000, 2);
    adminUI.AddProduct("chair", 20000, 1);
    adminUI.AddProduct("pen", 1000, 1);
    adminUI.AddProduct("pencil", -10, 1);
    adminUI.AddProduct("apple", 10, 0);
    adminUI.ListProduct();
    adminUI.EditProduct("tissue", 3000, 2);
    adminUI.EditProduct("chair", 20000, 5);
    adminUI.EditProduct("apple", 10, 1);
    adminUI.EditProduct("chair", 20000, -100);
    adminUI.EditProduct("pen", 1000, 0);
    adminUI.ListProduct();

#if TEST
    bool is_correct = compare_output(adminUI, "1.out");
    print_OX("Test 1", is_correct);
#endif
}

void test2(ClientUI& clientUI) {
#if MAIN
    std::cout << std::endl << "========== Test 2 ==========" << std::endl;
#endif
#if TEST
    clear_ostream_as_ostringstream(clientUI.getOs());
#endif

    clientUI.SignUp("Seonjun", "qwerty@");
    clientUI.SignUp("Dongho", "asdfh!");
    clientUI.Login("Dongho", "qwerty@");
    clientUI.Login("Seonjun", "qwerty@");
    clientUI.SignUp("Seokgyeong", "asdfh!");
    clientUI.AddToCart("tissue");
    clientUI.AddToCart("chair");
    clientUI.ListCart();
    clientUI.BuyAllInCart();
    clientUI.Login("Dongho", "asdfh!");
    clientUI.Logout();
    clientUI.AddToCart("chair");
    clientUI.Login("Dongho", "asdfh!");
    clientUI.Buy("chair");
    clientUI.ListCart();
    clientUI.Logout();
    clientUI.Logout();

#if TEST
    bool is_correct = compare_output(clientUI, "2.out");
    print_OX("Test 2", is_correct);
#endif
}


void test3(ClientUI& clientUI, AdminUI& adminUI) {
#if MAIN
    std::cout << std::endl << "========== Test 3 ==========" << std::endl;
#endif
#if TEST
    clear_ostream_as_ostringstream(clientUI.getOs());
#endif

    adminUI.AddProduct("A", 100000, 100);
    adminUI.AddProduct("B", 20, 100);
    adminUI.AddProduct("C", 30, 100);
    adminUI.AddProduct("D", 40, 100);
    adminUI.ListProduct();

    clientUI.SignUp("Alexa", "a");
    clientUI.SignUp("Bob", "b");
    clientUI.SignUp("Chloe", "c");
    clientUI.SignUp("David", "d");
    clientUI.SignUp("Emily", "e");
    clientUI.SignUp("Felix", "f");

    clientUI.Login("Alexa", "a");
    clientUI.Buy("A");
    clientUI.Buy("B");
    clientUI.Buy("C");
    clientUI.Buy("D");
    clientUI.Buy("C");
    clientUI.AddToCart("C");
    clientUI.Logout();

    clientUI.Login("Bob", "b");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("C");
    clientUI.AddToCart("A");
    clientUI.AddToCart("A");
    clientUI.AddToCart("A");
    clientUI.Logout();

    clientUI.Login("Chloe", "c");
    clientUI.Buy("B");
    clientUI.Buy("B");
    clientUI.Buy("C");
    clientUI.Buy("A");
    clientUI.Buy("D");
    clientUI.AddToCart("B");
    clientUI.AddToCart("C");
    clientUI.Logout();

    clientUI.Login("David", "d");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("B");
    clientUI.AddToCart("B");
    clientUI.AddToCart("A");
    clientUI.Logout();

    clientUI.Login("Emily", "e");
    clientUI.Buy("C");
    clientUI.Buy("C");
    clientUI.Buy("A");
    clientUI.AddToCart("C");
    clientUI.AddToCart("C");
    clientUI.Logout();

    clientUI.Login("Felix", "f");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.Buy("A");
    clientUI.AddToCart("D");
    clientUI.Logout();

    clientUI.Login("Alexa", "a");
    clientUI.RecommendProducts();

#if TEST
    bool is_correct = compare_output(clientUI, "3.out");
    print_OX("Test 3", is_correct);
#endif
}

int main() {
#if MAIN
    std::ostream& os = std::cout;
#endif
#if TEST
    std::ostringstream os;
#endif
    ShoppingDB db;
    AdminUI admin_ui(db, os);
    ClientUI client_ui(db, os);

    test1(admin_ui);
    test2(client_ui);
    test3(client_ui,admin_ui);
}
