package GeneticAlgoritm;

import java.util.ArrayList;
import java.util.List;

// This function is just a java recreation of this : https://stackoverflow.com/a/46584577
public class Roulette<T> {
    private class WeightedEntry {
        public double Weight;
        public T Item;

        public WeightedEntry(T item, double weight) {
            this.Weight = weight;
            this.Item = item;
        }
    }

    private double TotalWeight = 0.0f;
    private List<WeightedEntry> Entries = new ArrayList<WeightedEntry>();

    public void AddEntry(T Item, double Weight) {
        TotalWeight += Weight;
        Entries.add(new WeightedEntry(Item, Weight));
    }

    public void Clear() {
        this.TotalWeight = 0.0f;
        this.Entries.clear();
    }

    public T GetRandom() {
        while (true) {
            double r = Shared.GlobalRandom.nextDouble() * TotalWeight;
            for (var entry : this.Entries) {
                if (entry.Weight >= r) {
                    return entry.Item;
                }
            }
        }
    }

}
