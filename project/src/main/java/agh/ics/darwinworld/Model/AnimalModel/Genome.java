package agh.ics.darwinworld.Model.AnimalModel;

import java.util.Random;

public class Genome {
    protected String genes;
    protected int length;

    public Genome(int length){
        this.length = length;

        Random rand = new Random();
        StringBuilder randomGenes = new StringBuilder();
        for (int k = 0; k < length; k++) {
            int gene = rand.nextInt(8);
            randomGenes.append(Integer.toString(gene));
        }
        this.genes = randomGenes.toString();
    }

    public Genome(String genes, int length) {
        this.genes = genes;
        this.length = length;
    }

    public Genome getYoungGenome(Genome partnerGenome, int partition, boolean side, int n){
        String youngGenomeGenes;
        if(side){
            youngGenomeGenes = this.genes.substring(0, partition) + partnerGenome.genes.substring(partition, n);
        }
        else{
            youngGenomeGenes = partnerGenome.genes.substring(0,partition) + this.genes.substring(partition, n);
        }

        return new Genome(youngGenomeGenes, length);
    }

    public void twist(){
        Random rand = new Random();
        int newGene;
        String oldGene;
        int mutationPlace = rand.nextInt(length);
        oldGene = genes.substring(mutationPlace, mutationPlace + 1);
        do {
            newGene = rand.nextInt(8);
        } while (Integer.parseInt(oldGene) == newGene);

        char[] genesChars = genes.toCharArray();
        genesChars[mutationPlace] = (char) (newGene + '0');
        this.genes = String.valueOf(genesChars);
    }

    public String getGenes(){return genes;}
    public int getLength(){return length;}
}
