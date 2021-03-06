# RAPPAS :  Rapid Alignment-free Phylogenetic Placement via Ancestral Sequences


**This README contains short instructions to build and launch RAPPAS.**
**You will find more detailed intructions, tutorials and links related to phylogenetic placement on the [wiki page](https://github.com/blinard-BIOINFO/RAPPAS/wiki).**


## Description

RAPPAS (Rapid Alignment-free Phylogenetic PLacement via Ancestral Sequences) is a program dedicated to "Phylogenetic Placement" (PP) of metagenomic reads on a reference tree. As apposed to previous PP programs, RAPPAS uses a 2 step approach divided into a) the database build, and b) the placement itself.

The main advantage of RAPPAS is that it is alignment free, which means that after step (a) (the DB build) is performed, metagenomic reads can be directly placed on a referene tree _WITHOUT_ aligning them to the reference alignment on which the tree was built (as required by other approaches).

The second advantage of RAPPAS is its algorithm based on ancestal k-mer matches, making its execution time linear with respect to the length of the placed sequences.

![EU_flag](http://52.43.194.9/images/fc3849b.jpg) ![virogenesis_logo](http://52.43.194.9/images/32a5f46.png)
RAPPAS was funded from  the European Union’s Horizon 2020 research and innovation programme under grant agreement No 634650. (Virogenesis.eu)

## Installation

### Prerequisites

- RAPPAS compilation requires a clean JDK 1.8 javac compiler installation. Java >=1.8 is a compulsory requirement as some operations are based on Lambda expressions.
- Apache Ant is used to facilitate the compilation.

We provide instructions for Debian-based Linux distributions. For compiling Java sources with Apache Ant on other operating systems, please perform analogous operations on your system.

Using OpenJDK 1.8:
```
#install packages
sudo apt-get update
sudo apt-get install openjdk-8-jdk
#update relevant symlinks to make v1.8 default
sudo update-java-alternatives --set java-1.8.0-openjdk-amd64

```
Using the proprietary Oracle JDK 1.8:
```
#install packages
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
#update relevant symlinks to make v1.8 default
sudo apt-get install oracle-java8-set-default
```

Installation of Apache Ant:
```
sudo apt-get install ant
```

### Source download and compilation

```
#download git repository
git clone -b master https://github.com/blinard-BIOINFO/RAPPAS.git
#compile
cd RAPPAS && ant -f build-cli.xml
```
The executable RAPPAS.jar can then be found in the ./dist directory.




## Usage

### Reference Dataset
First, one has to prepare a reference dataset designed to answer a biological question. Typically, in the context of metagenomics and taxonomic identifications, a marker gene (16S rRNA, cox1, rbcl...) is used to build a reference species tree. This species tree is the basis for phylogenetic placement of marker gene(s).
For RAPPAS, the reference dataset is composed of:
1. A reference alignment of all sequences of this marker gene
2. A phylogenetic tree inferred from this reference alignment

Such reference marker gene datasets can be found, for instance from:
- "The All-Species Living Tree" Project (LTP, eukaryote rRNAs) :  <https://www.arb-silva.de/projects/living-tree/>,
- Greengenes (bacterial 16S) : <http://greengenes.secondgenome.com/>,
- Any marker database from EukRef: <http://eukref.org/databases/>,
- The curated database of Eukref : <http://eukref.org/databases/>,
- Or built internally in the lab.

### RAPPAS database build 

__Basic command__

```
java -Xmx8G -jar RAPPAS.jar -m b -s [nucl|prot] -b ARbinary -w workdir -r reference_alignment.fasta -t reference_tree.newick
```

where

option | expected value | description
--- | --- | ---
**-m <br/>(--mode)** | "b" | Invokes the "database build" process.
**-s <br/>(--states)** | "nucl" or "prot" | Set if we use a nucleotide or protein analysis.
**-b <br/>(--arbinary)** | binary of PhyML (>=v3.3) or PAML (>=4.9) | Set the path to the binary used for ancestral sequence reconstruction (see note below).
**-w <br/>(--workdir)** | directory | Set the directory to save the database in.
**-r <br/>(--refalign)** | file | The reference alignment, in fasta format.
**-t <br/>(--reftree)̀** | file | The reference tree, in newick format.

__Note on PhyML and PAML binaries__:
Currently, the following programs are fully supported by RAPPAS for generating ancestral sequence posterior probabilities:
- PhyML : Fastest & strongly recommended but may require lots of RAM.
- PAML  : Slower,  but requires less memory.

__Note on -Xm[x]G option__:
The process of database build can be memory intensive for values of k>=10.
To make RAPPAS run smoothly, allocate more memory (more heap) to the java process using the option -Xm[x]G where [x] is replaced by an integer value.
For instance, -Xm8G will extend the java heap to a maximum of 8Gb of memory, -Xmx16G will extend it to a maximum of 16Gb ... 


You can use the latest versions provided on the authors' websites. PhyML requires at least version 3.3 (see [PhyML GIT](https://github.com/stephaneguindon/phyml) ), but we recommand the _HACKED VERSIONS_ available in this git repository in the /depbin directory.
These are based on slightly modified sources of PhyML and PAML: no change in ML computations, but useless outputs are skipped, making the ancestral reconstruction process faster (in particular for PAML).

The reconstruction will result in the production of a directory structure and a database file in the given "workdir":

file or directory | description
--- | --- 
**[DBname].union** | The RAPPAS database itself.
**[workdir]/extended_tree** | Temporary files used at DB construction, allowing the exploration of ghost nodes.
**[workdir]/AR** | Temporary files used at DB construction, the raw output of PhyML or PAML.
**[workdir]/logs** | As the name says.


### Query placement

After building the RAPPAS DB, placement commands can be called numerous times on different query sequence datasets.
v1.00 of RAPPAS places 1,000,000 metagenomic of 150bp in ~30-40 minutes, using only a single core of a normal desktop PC.

```
java -Xmx8G -jar RAPPAS.jar -m p -s [nucl|prot] -w workdir -d database.union -q queries.fasta 
```

where

option | expected value | description
--- | --- | ---
**-m <br/>(--mode)** | "p" | Invokes the "placement" process.
**-s <br/>(--states)** | "nucl" or "prot" | Set if we use a nucleotide or protein analysis.
**-w <br/>(--workdir)** | directory | Set the directory to save the database in.
**-d <br/>(--database)** | file | the *.union file created at previous DB build step.
**-q <br/>(--queries)** | file | The query reads, in fasta format.

__Note on -Xm[x]G option__:
Reuse the value used in the database build phase, as loading the database will basically require the same amount of memory.

The *.jplace describing the placements of all queries will be written in the ./workdir/logs directory.

__To know more about :__
- the [jplace format](http://journals.plos.org/plosone/article?id=10.1371/journal.pone.0031009).
- the [exploitation of phylogenetic placement results](https://matsen.github.io/pplacer/generated_rst/guppy.html#introduction) (OTU alpha diversity, Unifrac-like measures...).

### Other options

__Outputs  options:__
Options are related to the Jplace file outputs which resumes the placement results.
They are analogs to PPlacer options. 

option | expected value {default} | description
--- | --- | ---
**--keep-at-most** | integer>=1  {7} | Maximum number of placements reported per query in the jplace output. (p mode)
**--keep-factor** | float in ]0;1]  {0.01} | Report placement with likelihood_ratio higher than (factor x best_likelihood_ratio). (p mode)
**--write-reduction** | file | Write reduced alignment to a file (see --ratio-reduction). (b mode)

__Algorithm  options:__
Options are related to the Jplace file outputs which resumes the placement results.
They are analogs to PPlacer options. 

option | expected value {default} | description
--- | --- | ---
**-a <br/>(--alpha)** | float in ]0,Inf] {1.0} | Shape parameter used in AR. (b mode)
**-c <br/>(--categories)** | int in [1,Inf] {4} | # categories used in AR. (b mode)
**-k** | integer>=3 {8} | The k-mer length used at DB build.
**-m <br/>(--model)** | string {GTR\|LG} | Model used in AR, one of the following: (for nucl) JC69, HKY85, K80, F81, TN93, GTR ; (for amino) LG, WAG, JTT, Dayhoff, DCMut, CpREV, mMtREV, MtMam, MtArt (b mode)
**--arparameters** | string | Parameters passed to the software used for anc. seq. reconstuct. Overrides -a,-c,-m options. Value must be quoted by ' or ". Do not set options -i,-u,--ancestral (managed by RAPPAS). PhyML example: "-m HIVw -c 10 -f m -v 0.0 --r_seed 1" (b phase)
**--convertUOX** | none | U,O,X amino acids are converted to C,L,- to allow correct ancestral reconstruction (b mode)
**--force-root**  | none | Root input tree if non rooted. (b mode)
**--ratio-reduction** | float in ]0,1] {0.999} |Ratio for alignment reduction, i.e. sites holding >99.9% gaps are ignored. (b mode)
**--no-reduction** |  none  | Do not operate alignment reduction. This will keep all sites of input reference alignment but may produce erroneous ancestral k-mers. (b mode)
**--gap-jump-thresh** | float in ]0,1] {0.3}| Gap ratio above which gap jumps are activated, for instance if the reference alignment hold more than 30% of gaps.
**--omega** | float in ]0,#states] {1.0} | Alpha modifier levelling the proba threshold used in ancestral words filtering. (b mode)


__Debug options:__
Avoid using debug options if you are not involved in RAPPAS development.

option | expected value {default} | description
--- | --- | ---
**--ardir** | directory | Skips ancestral sequence reconstruction, and uses outputs of PhyML or PAML present in the specified directory. (b mode)
**--extree**  | directory |  Skips ghost nodes injection, and use injected trees present in the specified directory. (b mode)
**--nsbound** | float in ]-Inf,O[ | Forces score bound. (p mode)
**--dbinram** | none | Operate "b" followed by "p" mode, without saving DB to files and placing directly queries given via -q .
**--calibration** | none |  Prototype score calibration on random anc. kmers. (b mode).
**--do-n-jumps** | none |   Shifts to n jumps. (b mode) 
**--no-gap-jumps** | none |  Deactivate k-mer gap jumps, even if reference alignment has a proportion of gaps higher than "--gap-jump-thresh". (b mode) 


## License

RAPPAS is available under the GPLv3 license.


