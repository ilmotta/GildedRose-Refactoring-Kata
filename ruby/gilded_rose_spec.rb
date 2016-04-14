require File.join(File.dirname(__FILE__), 'gilded_rose')

describe GildedRose do
  describe '#update_quality' do
    it 'does not change the name' do
      item = Item.new('foo', sell_in=0, quality=0)
      described_class.new([item]).update_quality
      expect(item.name).to eq 'foo'
    end

    it 'degrades twice as fast after the sell by date has passed' do
      item = Item.new('foo', sell_in=0, quality=5)
      actual = described_class.new([item]).update_quality
      expect(item.sell_in).to eq -1
      expect(item.quality).to eq 3
    end

    it 'caps the quality to a minimum of zero' do
      item = Item.new('foo', sell_in=20, quality=1)
      subject = described_class.new([item])
      2.times { subject.update_quality }
      expect(item.quality).to eq GildedRose::MIN_QUALITY
    end

    context 'item "Sulfuras, Hand of Ragnaros"' do
      let :item  do
        Item.new('Sulfuras, Hand of Ragnaros', sell_in=20, quality=10)
      end

      subject { described_class.new([item]) }

      it 'never gets old' do
        2.times { subject.update_quality }
        expect(item.sell_in).to eq 20
      end

      it 'never decreases in quality' do
        2.times { subject.update_quality }
        expect(item.quality).to eq 10
      end
    end

    shared_examples 'increases the Quality the older it gets' do
      it 'increases the item quality' do
        item.quality = 1
        subject.update_quality
        expect(item.sell_in).to eq 19
        expect(item.quality).to eq 2
      end

      it 'caps the quality to a maximum of 50' do
        item.quality = 48
        3.times { subject.update_quality }
        expect(item.quality).to eq GildedRose::MAX_QUALITY
      end
    end

    context 'item "Aged Brie"' do
      let(:item) { Item.new('Aged Brie', sell_in=20, quality=0) }
      subject { described_class.new([item]) }
      include_examples 'increases the Quality the older it gets'
    end

    context 'item "Backstage passes to a TAFKAL80ETC concert"' do
      let(:name) { 'Backstage passes to a TAFKAL80ETC concert' }
      let(:item) { Item.new(name, sell_in=20, quality=20) }
      subject { described_class.new([item]) }
      include_examples 'increases the Quality the older it gets'

      it 'increases the quality by 2 when there are 10 days or less left' do
        item.sell_in = 10
        subject.update_quality
        expect(item.quality).to eq 22
      end

      it 'does not increase the quality by 2 when there are 11 days left' do
        item.sell_in = 11
        item.quality = 20
        subject.update_quality
        expect(item.quality).to eq 21
      end

      it 'increases the quality by 3 when there are 5 days or less left' do
        item.sell_in = 5
        subject.update_quality
        expect(item.quality).to eq 23
      end

      it 'does not increase the quality by 3 when there are 6 days left' do
        item.sell_in = 6
        subject.update_quality
        expect(item.quality).to eq 22
      end

      it 'drops the quality to zero after the concert' do
        item.sell_in = 0
        subject.update_quality
        expect(item.quality).to eq 0

        item.sell_in = -1
        item.quality = 10
        subject.update_quality
        expect(item.quality).to eq 0
      end
    end
  end
end
