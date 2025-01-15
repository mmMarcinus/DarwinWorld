package agh.ics.darwinworld.AnimalModel;

import java.util.Random;

public class ChangeGenome extends Genome {

    public ChangeGenome(int length) {
        super(length);
    }

    public ChangeGenome(String genes, int length) {
        super(genes, length);
    }

    @Override
    public void twist() {
        //część normalna
        Random rand = new Random();
        int newGene;
        String oldGene;
        int mutationPlace = rand.nextInt(length);
        do {
            newGene = rand.nextInt(8);
            oldGene = genes.substring(newGene, newGene + 1);
        } while (Integer.parseInt(oldGene) == newGene);

        char[] genesChars = genes.toCharArray();
        genesChars[mutationPlace] = (char) (newGene + '0');
        this.genes = String.valueOf(genesChars);

        int firstChangePlace;
        int secondChangePlace;

        //część bonusowa zamiany (NIE WIEM CZY MA SIĘ DZIAĆ ZAWSZE, CZY TYLKO CO JAKIŚ CZAS WIĘC JEST TAK NARAZIE)
        firstChangePlace = rand.nextInt(length);
        do {
            secondChangePlace = rand.nextInt(length);
        } while (firstChangePlace == secondChangePlace);

        genesChars = this.genes.toCharArray();
        char buf = genesChars[firstChangePlace];
        genesChars[firstChangePlace] = genesChars[secondChangePlace];
        genesChars[secondChangePlace] = buf;
        this.genes = String.valueOf(genesChars);
    }

}