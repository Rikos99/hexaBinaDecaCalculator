package com.rikoz99.hexadecimalnibinarnikalkulacka;

import androidx.annotation.NonNull;

public class ConversionEntryModel
{
    private int id;
    private String number;
    private int from;
    private int to;
    private String result;

    public ConversionEntryModel(int id, String number, int from, int to)
    {
        this.id = id;
        this.number = number;
        this.from = from;
        this.to = to;

        //převedení čísla na číslo správné soustavy

        if(this.to == 0) //Dec
        {
            this.result = number;
        }
        else if(this.to == 1) //Bin
        {
            this.result = Integer.toString(Integer.parseInt(number), 2);
        }
        else if (this.to == 2) //Hex
        {
            this.result = Integer.toString(Integer.parseInt(number), 16);
        }
    }

    public ConversionEntryModel() {}

    public String getNumber() {return number;}

    public void setNumber(String number) {this.number = number;}

    public int getFrom() {return from;}

    public void setFrom(int from) {this.from = from;}

    public int getTo() {return to;}

    public void setTo(int to) {this.to = to;}

    public String getResult() {return result;}

    public void setResult(String result) {this.result = result;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    @NonNull
    @Override
    public String toString()
    {
        String resultToHistory = "";
        if(this.from == 0) //Dec
        {
            resultToHistory = "(Dec) ";
        }
        else if(this.from == 1) //Bin
        {
            resultToHistory = "(Bin) ";
        }
        else if (this.from == 2) //Hex
        {
            resultToHistory = "(Hex) ";
        }
        resultToHistory += this.number + " -> ";

        if(this.to == 0) //Dec
        {
            resultToHistory += "(Dec) ";
        }
        else if(this.to == 1) //Bin
        {
            resultToHistory += "(Bin) ";
        }
        else if (this.to == 2) //Hex
        {
            resultToHistory += "(Hex) ";
        }
        resultToHistory += result + "\n";
        return resultToHistory;
    }


}
