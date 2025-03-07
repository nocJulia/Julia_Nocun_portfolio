#Minimalna wersja cmake konieczna do zbudowania tego projektu
cmake_minimum_required(VERSION 3.4)

#Parametry konfiguracyjne platformy programistycznej i flagi kompilatora
set(CMAKE_CXX_COMPILER g++)#g++ jako kompilator C++
set(CMAKE_C_COMPILER gcc)#gcc jako kompilator C
set(CMAKE_CXX_STANDARD 11)#Standard C++ 11, bez tego parametru g++ użyje wartości domyślnej. Dla g++ od wersji 6 jest to C++14
set(CMAKE_CXX_STANDARD_REQUIRED ON)#Określony wyżej standard C++ jest wymagany

#Profil debug i maksymalny poziom debug info g3 oraz format debug info na dwarf-4 (musi być gdb w wersji 7). BUILD_TYPE może być ustawiony na:
#empty
#Debug
#Release
#RelWithDebInfo
#MinSizeRel
#Ustawiono dodatkowo flagi debug generujące informację dla debuggera
set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_C_FLAGS_DEBUG "-g3 -gdwarf-4")
set(CMAKE_CXX_FLAGS_DEBUG "-g3 -gdwarf-4")

#Ustaw nazwę projektu i język na C++

project(BaybysittingAgency)

enable_language(CXX)

#W specjalnym pliku compile_commands.json można podejrzeć wszystkie operacje kompilatora wraz z parametrami, 

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

set(CMAKE_VERBOSE_MAKEFILE TRUE)

#Source files here

set(SOURCE_FILES
        include/Model/Child.h
		src/Model/Child.cpp
		src/main.cpp
        include/Model/Address.h
		src/Model/Address.cpp
		include/Model/Babysitter.h
		src/Model/Babysitter.cpp
		include/typedefs.h
		include/Model/BabysitterType.h
		include/Model/Reservation.h
		src/Model/Reservation.cpp
		include/Model/NewbornsBabysitter.h
		include/Model/ToddlersBabysitter.h
		include/Model/RemainingAgeRangeBabysitter.h
		src/Model/ToddlersBabysitter.cpp
		src/Model/NewbornsBabysitter.cpp
		src/Model/RemainingAgeRangeBabysitter.cpp
		include/Repository/Repository.h
		include/Managers/ChildrenManager.h
		src/Managers/ChildrenManager.cpp
		include/Managers/ReservationsManager.h
		include/Managers/BabysittersManager.h
		include/Exceptions/BadArguments.h
		src/Managers/BabysittersManager.cpp
		src/Managers/ReservationsManager.cpp
		include/Model/BabysittingAgency.h
		src/Model/BabysittingAgency.cpp
		include/Exceptions/LogicError.h)

add_executable(BabysittingAgency ${SOURCE_FILES})

target_include_directories(BabysittingAgency PUBLIC include)

enable_testing()

#TEST
find_package(Boost 1.65 COMPONENTS "unit_test_framework" REQUIRED)

include_directories(
		include
		${Boost_INCLUDE_DIRS}
)

#Test source files here
set(SOURCE_TEST_FILES
		src/Model/Child.cpp
		src/Model/Address.cpp
		src/Model/Babysitter.cpp
		src/Model/Reservation.cpp
		src/Model/RemainingAgeRangeBabysitter.cpp
		src/Model/NewbornsBabysitter.cpp
		src/Model/ToddlersBabysitter.cpp
		test/ChildTest.cpp
		test/master.cpp
		test/AddressTest.cpp
		test/BabysitterTest.cpp
		test/BabysitterTypesTest.cpp
		test/ReservationTest.cpp
        )

add_executable(TestProject ${SOURCE_TEST_FILES})

target_include_directories(TestProject PUBLIC include)

target_link_libraries(TestProject
		${Boost_UNIT_TEST_FRAMEWORK_LIBRARY})

add_test(NAME Test COMMAND TestProject
		--report_level=detailed
		--log_level=all
		--color_output=yes)

add_custom_target(check ${CMAKE_COMMAND} -E env CTEST_OUTPUT_ON_FAILURE=1 BOOST_TEST_LOG_LEVEL=all
		${CMAKE_CTEST_COMMAND} -C $<CONFIG> --verbose
		WORKING_DIRECTORY ${CMAKE_BINARY_DIR})