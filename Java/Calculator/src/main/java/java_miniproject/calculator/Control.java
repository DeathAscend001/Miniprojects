/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_miniproject.calculator;

import javax.swing.*;

/**
 *
 * @author trias
 */
public class Control
{
    public void SetButtonStatus(JButton[] Mod_Button, Boolean Flag, JButton DPoint, Boolean Status)
    {
        DPoint.setEnabled(Status);
        for(JButton b : Mod_Button)
        {
            b.setEnabled(Flag);
        }
    }
    
    private String Arithmetic(String E, char operator)
    {
        float Value = 0;
        try
        {
            String[] Numbers = null;
            switch(operator)
            {
                case '+':
                    Numbers = E.split("\\+");
                    for(int i = 0; i < Numbers.length; i++)
                    {
                        Value += Float.parseFloat(Numbers[i]);
                    }
                    break;
                case '-':
                    Numbers = E.split("-");
                    Value = Float.parseFloat(Numbers[0]) - Float.parseFloat(Numbers[1]);
                    break;
                case '*':
                    Numbers = E.split("\\*");
                    Value = 1;
                    for(int i = 0; i < Numbers.length; i++)
                    {
                        Value *= Float.parseFloat(Numbers[i]);
                    }
                    Value = Math.round(Value*100000000); Value = Value/100000000;
                    break;
                case '/':
                    Numbers = E.split("\\/");
                    float N = 1, D = 1;
                    for(int i = 0; i < Numbers.length; i++)
                    {
                        if(i % 2 == 0) // Numerator
                        {
                            N *= Float.parseFloat(Numbers[i]);
                        }
                        else // Denominator
                        {
                            D *= Float.parseFloat(Numbers[i]);
                        }
                    }
                    Value = N/D;
                    break;
            }
        }
        catch(NumberFormatException e)
        {
            return "Error";
        }
        return Float.toString(Value);
    }
    
    private String Fixer(String Res, char operator)
    {
        String Ret_Str = "";
        int s_index = 0;
        int e_index = 0;
        try
        {
            switch(operator)
            {
                case '+':
                    for(int i = 0; i < Res.length(); i++)
                    {
                        if(Res.charAt(i) == operator)
                        {
                            for(int j = i+1; j < Res.length(); j++)
                            {
                                e_index = j+1;
                                if(Res.charAt(j) == '*' || Res.charAt(j) == '-' || Res.charAt(j) == '/')
                                {
                                    e_index = j;
                                    break;
                                }
                            }
                            Ret_Str = Res.substring(0, s_index) + Arithmetic(Res.substring(s_index, e_index), operator) + Res.substring(e_index, Res.length());
                        }
                        if(Res.charAt(i) == '*' || Res.charAt(i) == '-' || Res.charAt(i) == '/')
                        {
                            s_index = i+1;
                        }
                    }
                    break;
                case '-':
                    for(int i = 0; i < Res.length(); i++)
                    {
                        if(Res.charAt(i) == operator)
                        {
                            for(int j = i+1; j < Res.length(); j++)
                            {
                                e_index = j+1;
                                if(Res.charAt(j) == '+' || Res.charAt(j) == '*' || Res.charAt(j) == '/')
                                {
                                    e_index = j;
                                    break;
                                }
                            }
                            System.out.println(Res.substring(s_index, e_index));
                            Ret_Str = Res.substring(0, s_index) + Arithmetic(Res.substring(s_index, e_index), operator) + Res.substring(e_index, Res.length());
                        }
                        if(Res.charAt(i) == '+' || Res.charAt(i) == '*' || Res.charAt(i) == '/')
                        {
                            s_index = i+1;
                        }
                    }
                    break;
                case '*':
                    for(int i = 0; i < Res.length(); i++)
                    {
                        if(Res.charAt(i) == operator)
                        {
                            for(int j = i+1; j < Res.length(); j++)
                            {
                                e_index = j+1;
                                if(Res.charAt(j) == '+' || Res.charAt(j) == '-' || Res.charAt(j) == '/')
                                {
                                    e_index = j;
                                    break;
                                }
                            }
                            Ret_Str = Res.substring(0, s_index) + Arithmetic(Res.substring(s_index, e_index), operator) + Res.substring(e_index, Res.length());
                        }
                        if(Res.charAt(i) == '+' || Res.charAt(i) == '-' || Res.charAt(i) == '/')
                        {
                            s_index = i+1;
                        }
                    }
                    break;
                case '/':
                    for(int i = 0; i < Res.length(); i++)
                    {
                        if(Res.charAt(i) == operator)
                        {
                            for(int j = i+1; j < Res.length(); j++)
                            {
                                e_index = j+1;
                                if(Res.charAt(j) == '+' || Res.charAt(j) == '-' || Res.charAt(j) == '*')
                                {
                                    e_index = j;
                                    break;
                                }
                            }
                            Ret_Str = Res.substring(0, s_index) + Arithmetic(Res.substring(s_index, e_index), operator) + Res.substring(e_index, Res.length());
                        }
                        if(Res.charAt(i) == '+' || Res.charAt(i) == '-' || Res.charAt(i) == '*')
                        {
                            s_index = i+1;
                        }
                    }
                    break;
            }
        }
        catch(Exception e)
        {
            return "Error";
        }
        return Ret_Str;
    }
    //
    public String Equation(String Res)
    {
        try
        {
            // Division
            while(Res.contains("/"))
            {
                Res = Fixer(Res, '/');
            }
            // Multiplication
            while(Res.contains("*"))
            {
                Res = Fixer(Res, '*');
            }
            // Subtraction
            while(Res.contains("-"))
            {
                if(Res.charAt(0) != '-')
                {
                    // Check For Double Minus //
                    for(int i = 0; i < Res.length(); i++)
                    {
                        if(Res.charAt(i) == '-' && Res.charAt(i+1) == '-')
                        {
                            Res = Res.substring(0, i) + '+' + Res.substring(i+2, Res.length());
                        }
                        else if(Res.charAt(i) == '+' && Res.charAt(i+1) == '-')
                        {
                            Res = Res.substring(0, i) + '-' + Res.substring(i+2, Res.length());
                        }
                    }
                    Res = Fixer(Res, '-');
                }
                else
                {
                    System.out.println(Res);
                    break;
                }
            }
            // Addition
            while(Res.contains("+"))
            {
                Res = Fixer(Res, '+');
            }
        }
        catch(Exception e)
        {
            return "Error";
        }
        return Res;
    }
}
