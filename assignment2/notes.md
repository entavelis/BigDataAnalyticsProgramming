##Notes

To implement:

- Hash Function
- Many Hash Functions
- Tokenize Input
- Change Shingle Nature (Done Shingles Token)

##Questions

- Different types of shingles?
- How to measure accuracy?
- Do parameters work on whichever input?

##Choices when implementing
- What to do at initialization and what to store: Easiness of Configurations
- Checked what's private and what's public
- Murmur Hash: Bytes in stead of Strings
- What Happens when we have collisions in many bands
- Should sig be an array or a hash net? Array fixed size!
- Similarity pairs from smaller to the biggest!
- add equals() and hashCode() in similarity pairs in order to integrate better with HashSet
- Bands number and Rows NOs
- Number of shingles: few -> not much expressiveness more recall less precision,
        , too many, sparse matrix -> less meaningful permutations  more prec less recall
 

##Experiments
Less Bands are faster

###Parameters
- k of Shingles 


### TO DO:
K effect
different thresholds
sampling
reservoir sampling
or and analysis
how does the behavior changes w diff maxfiles





