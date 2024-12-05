#include <algorithm>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>
#include <sstream>

#include "App.h"

namespace fs = std::filesystem;

bool test(std::string testName, fs::path input, fs::path output) {
    std::ifstream ifs(input);
    std::ostringstream oss;

    App app(ifs, oss);
    app.run();

    std::string outputApp = oss.str();

    std::ifstream ifsAnswer(output);
    std::string outputAnswer((std::istreambuf_iterator<char>(ifsAnswer)), (std::istreambuf_iterator<char>()));
    bool success = outputApp == outputAnswer;

    if (!success) {
        std::cout << "%%%%%%%%%%%%%%%%%%% " << testName << ": App    %%%%%%%%%%%%%%%%%%%" << std::endl;
        std::cout << outputApp << std::endl;
        std::cout << "%%%%%%%%%%%%%%%%%%% " << testName << ": Answer %%%%%%%%%%%%%%%%%%%" << std::endl;
        std::cout << outputAnswer << std::endl;
        std::cout << "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" << std::endl;
        std::cout << std::endl;
    }
    return success;
}

int main() {
    std::stringstream ss;
    ss << "Test 1: ";
    ss << (test("Test 1 - 1", "test/sub1/1.in", "test/sub1/1.out") ? "O" : "X") << " | ";
    ss << (test("Test 1 - 2", "test/sub1/2.in", "test/sub1/2.out") ? "O" : "X") << " | ";
    ss << (test("Test 1 - 3", "test/sub1/3.in", "test/sub1/3.out") ? "O" : "X") << " | ";
    ss << (test("Test 1 - 4", "test/sub1/4.in", "test/sub1/4.out") ? "O" : "X") << " | ";
    ss << (test("Test 1 - 5", "test/sub1/5.in", "test/sub1/5.out") ? "O" : "X") << std::endl;

    ss << "Test 2: ";
    ss << (test("Test 2 - 1", "test/sub2/1.in", "test/sub2/1.out") ? "O" : "X") << std::endl;

    ss << "Test 3: ";
    ss << (test("Test 3 - 1", "test/sub3/1.in", "test/sub3/1.out") ? "O" : "X") << " | ";
    ss << (test("Test 3 - 2", "test/sub3/2.in", "test/sub3/2.out") ? "O" : "X") << " | ";
    ss << (test("Test 3 - 3", "test/sub3/3.in", "test/sub3/3.out") ? "O" : "X") << std::endl;

    ss << "Test 4: ";
    ss << (test("Test 4 - 1", "test/sub4/1.in", "test/sub4/1.out") ? "O" : "X") << " | ";
    ss << (test("Test 4 - 2", "test/sub4/2.in", "test/sub4/2.out") ? "O" : "X") << " | ";
    ss << (test("Test 4 - 3", "test/sub4/3.in", "test/sub4/3.out") ? "O" : "X") << " | ";
    ss << (test("Test 4 - 4", "test/sub4/4.in", "test/sub4/4.out") ? "O" : "X") << std::endl;
    std::cout << ss.str();
}
