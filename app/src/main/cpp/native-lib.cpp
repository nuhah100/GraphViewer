//
// Created by me on 12/09/2020.
//
#include <cstring>
#include <jni.h>
#include <android/log.h>
#include <string>
#include <stdlib.h>
#include <string>
#include <regex>
#include <iterator>
#include <cmath>
#include <string_view>
#include <algorithm>

#define OpOrderLen 3


// TODO ADD MULTITHREADING!!!!
class Interpeter
{
    std::string operators = "^*/+-";

    // Replacement for sign '-' which is not operator.
    std::string subReplace = "`";
    std::string minusSign = "-";

    std::string func;

    std::regex signGet;
    std::regex spaces;
public:
    Interpeter()
    {
        signGet = "(((?=[-+*^/])[-])([-]{0}))";
        spaces = "\\s+";
    }
    double calculate(std::string rawFunc) {
        std::transform(rawFunc.begin(), rawFunc.end(), rawFunc.begin(), ::tolower);
        //std::string func = rawFunc.toLowerCase();
        return eval(rawFunc);
    }
    double eval(std::string expRaw) {
        std::string exp;
        //std::regex_replace(std::back_inserter(exp), expRaw.begin(), expRaw.end(), "\\s+", "");
        //std::regex_replace(std::back_inserter(exp), expRaw.begin(), expRaw.end(), "((?=[-+*^/])[-])([-]{0})", subReplace);
        exp = std::regex_replace(expRaw, spaces, "");
        exp = std::regex_replace(exp, signGet, subReplace.c_str());
        double res;

        while (exp.find("(") != std::string::npos || exp.find(")") != std::string::npos)
        {
            size_t openBracket, closeBracket;
            openBracket = exp.rfind('(');
            closeBracket = exp.find(')');
            double tempRes = eval(exp.substr(openBracket + 1, closeBracket - openBracket));
            exp = exp.substr(0, openBracket) + std::to_string(tempRes) + exp.substr(closeBracket + 1);
        }
        res = calculateExp(exp);
        return res;
    }
private:
    double calculateExp(std::string exp) {
        double res = 0;

        // If no signs parse and exit.
        if (isOccurs(exp, operators.c_str()) == std::string::npos)
            return atof(exp.c_str());

        // Remove "+-" strings.
        //exp = exp.replace("+-","-");

        // Order of signs.
        const std::string opOrder[3] = { "^","*/","+-" };


        // Looping for each couple signs.
        for (std::string signs : opOrder)
        {
            std::string expNoSpecial = std::regex_replace(exp,std::regex(subReplace), minusSign);
            //std::regex_replace(std::back_inserter(expNoSpecial), exp.begin(), exp.end(), subReplace, '-');

            // While it's still not exp and include signs.
            while (!tryParseDouble(expNoSpecial) && isOneContains(exp, signs.c_str())) {
                // Get sign place.
                size_t signPlace = isOccurs(exp, signs.c_str());
                // Get actual sign.
                char sign = exp[signPlace];


                // Get string before the sign.
                std::string_view beforeRaw(exp.c_str(), signPlace);
                // Get string after the sign.
                std::string_view afterRaw(exp.c_str() + signPlace + 1);
                // So we can change them.
                std::string_view before = beforeRaw, after = afterRaw;

                // Check if there is sign before our sign.
                size_t beforeI = isOccursLast(beforeRaw, operators.c_str());
                // If there is sign before.
                if (beforeI != std::string::npos) {
                    // If it's '-' and it's the first, so it's negative sign.
                    //if(beforeRaw.charAt(beforeI) == '-' && beforeI == 0) // For Minus before number.
                     //   beforeI--;
                    // Get the string after the sign.
                    before = beforeRaw.substr(beforeI + 1);
                }

                // Check if sign after.
                size_t afterI = isOccurs(afterRaw, operators.c_str());
                // If it's minus sign of number.
                //if(afterI == 0 && afterRaw.charAt(afterI) == '-')
                //    afterI = isOccurs(afterRaw.substring(1),operators.toCharArray());
                // If sign exists.
                if (afterI != std::string::npos) {
                    // Get the string before it.
                    after = afterRaw.substr(0,afterI);
                }

                // Return signs to original
                std::string beforeCal = std::regex_replace(std::string(before),std::regex(subReplace), minusSign);
                std::string afterCal = std::regex_replace(std::string(after), std::regex(subReplace), minusSign);
                //std::regex_replace(std::back_inserter(before), before.begin(), before.end(), subReplace, "-");
                //std::regex_replace(std::back_inserter(after), after.begin(), after.end(), subReplace, "-");

                // Only if there is before and after, and not "+2".
                if (afterCal.size() > 0 && beforeCal.size() > 0) {
                    //Check What is the sign.
                    switch (sign) {
                    case '^': {
                        res = pow(std::stod(std::string(beforeCal)), std::stod(std::string(afterCal)));
                        break;
                    }
                    case '*': {
                        res = std::stod(std::string(beforeCal)) * std::stod(std::string(afterCal));
                        break;
                    }
                    case '/': {
                        if (std::stod(std::string(afterCal)) == 0)
                            throw new std::exception;
                        res = std::stod(std::string(beforeCal)) / std::stod(std::string(afterCal));
                        break;
                    }
                    case '+': {
                        res = std::stod(std::string(beforeCal)) + std::stod(std::string(afterCal));
                        break;
                    }
                    case '-': {
                        res = std::stod(std::string(beforeCal)) - std::stod(std::string(afterCal));
                        break;
                    }
                    } // Calculate.
                }
                std::string resStr = std::to_string(res);
                //std::regex_replace(std::back_inserter(resStr), resStr.begin(), resStr.end(), "((?=[-+*^/])[-])([-]{0})", subReplace.c_str());
                resStr = std::regex_replace(resStr, signGet, subReplace.c_str());
                // For string addition.
                std::string temp;
                // If there is string beforeCal the sign.
                if (beforeI != std::string::npos)
                    temp.append(beforeRaw.substr(0,beforeI));
                // Added the result.
                temp.append(std::to_string(res));
                // If there is string afterCal the sign.
                if (afterI != std::string::npos)
                    temp.append(afterRaw.substr(afterI));
                // Parse to string.
                //std::regex_replace(std::back_inserter(exp), temp.begin(), temp.end(), "((?=[-+*^/])[-])([-]{0})", subReplace.c_str());
                exp = std::regex_replace(temp, signGet, subReplace.c_str());
            }
        }

        // Parse the exp because no signs involved:)
        //std::regex_replace(std::back_inserter(exp), exp.begin(), exp.end(), subReplace.c_str(), "-");
        exp = std::regex_replace(exp, std::regex(subReplace), minusSign);
        return std::stod(exp);
    }


    // Function to check if string can parse as double.
    bool tryParseDouble(std::string in) {
        try {
            size_t read = 0;
            double res = std::stod(in, &read);
            if (in.size() != read)
                return false;
        }
        catch (std::invalid_argument) {
            return false;
        }
        return true;
    }

    // Check if string contains one char of char array.
    bool isOneContains(std::string exp, const char* arr) {
        bool isOne = false;

        for (int i = 0; i < strlen(arr) && !isOne; i++)
            isOne = exp.find(arr[i]) != std::string::npos;

        return isOne;
    }

    // Return the index of closest char of char array to start, return '-1' if not found.
    size_t isOccurs(std::string_view str, const char* signs)
    {
        size_t isOccur = std::string::npos;
        for (size_t i = 0; i < strlen(signs); i++)
            isOccur = fmin(str.find(signs[i]), isOccur);
        return isOccur;
    }

    // Return the index of closest char of char array to end, return '-1' if not found.
    size_t isOccursLast(std::string_view str, const char* signs)
    {
        int isOccur = -1;

        for (size_t i = 0; i < strlen(signs); i++)
            isOccur = str.find(signs[i]) != std::string::npos ? fmax(isOccur, (int)str.find(signs[i])) : -1;

        return (size_t)isOccur;
    }


};

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

extern "C" JNIEXPORT jdouble JNICALL
Java_classes_Graph_CalculateExp(JNIEnv *env, jobject thiz, jstring expRaw)
{
Interpeter in;
    std::string exp = jstring2string(env, expRaw);
    double res;
    res = in.calculate(exp);
    return res;
}
/*
extern "C" JNIEXPORT jint JNICALL
Java_classes_Graph_Alloc(JNIEnv *env, jobject thiz ){
    in = new Interpeter;
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_classes_Graph_Free(JNIEnv *env, jobject thiz ){
    delete in;
    return 0;
}*/

