cmake_minimum_required(VERSION 3.0.0)
project(problem1 VERSION 0.1.0)

include(CTest)
enable_testing()

add_executable(problem1_main test.cpp UI.cpp AdminUI.cpp ClientUI.cpp ShoppingDB.cpp Product.cpp User.cpp Membership.cpp Membership.h)
add_executable(problem1_test test.cpp UI.cpp AdminUI.cpp ClientUI.cpp ShoppingDB.cpp Product.cpp User.cpp Membership.cpp Membership.h)

target_compile_definitions(problem1_main PRIVATE MAIN)
target_compile_definitions(problem1_test PRIVATE TEST)

file(REMOVE_RECURSE ${CMAKE_BINARY_DIR}/test)
file(COPY test DESTINATION .)

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
set(CMAKE_CXX_FLAGS "-std=c++17")
include(CPack)
